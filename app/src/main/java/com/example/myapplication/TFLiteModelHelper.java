package com.example.myapplication;

import android.content.Context;
import android.content.res.AssetFileDescriptor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Map;

public class TFLiteModelHelper {
    private final Interpreter tflite;
    private final float[] mean;
    private final float[] std;

    public TFLiteModelHelper(Context context) throws IOException {
        tflite = new Interpreter(loadModelFile(context));
        Map<String, float[]> meanStdValues = getMeanStdValues(context);
        mean = meanStdValues.get("mean");
        std = meanStdValues.get("std");
    }

    private ByteBuffer loadModelFile(Context context) throws IOException {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd("model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();

        ByteBuffer modelBuffer = ByteBuffer.allocateDirect((int) declaredLength).order(ByteOrder.nativeOrder());
        fileChannel.read(modelBuffer, startOffset);
        modelBuffer.rewind();
        return modelBuffer;
    }

    private Map<String, float[]> getMeanStdValues(Context context) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("mean_std_values.json"), Charset.forName("UTF-8")));
        Type type = new TypeToken<Map<String, float[]>>() {}.getType();
        return new Gson().fromJson(reader, type);
    }

    public int predict(float[] inputFeatures) {
        // Normalize input features using mean and std values
        for (int i = 0; i < inputFeatures.length; i++) {
            inputFeatures[i] = (inputFeatures[i] - mean[i]) / std[i];
        }

        // Prepare input tensor
        ByteBuffer inputBuffer = ByteBuffer.allocateDirect(4 * inputFeatures.length).order(ByteOrder.nativeOrder());
        for (float value : inputFeatures) {
            inputBuffer.putFloat(value);
        }

        // Prepare output tensor
        ByteBuffer outputBuffer = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder());

        // Run inference
        tflite.run(inputBuffer, outputBuffer);

        // Get the output
        outputBuffer.rewind();
        float output = outputBuffer.getFloat();

        // Convert the output to a class label
        return (output > 0.5) ? 1 : 0;
    }
}
