package com.benmohammad.rxsmoke.utils;

import android.util.Pair;

import com.benmohammad.rxsmoke.AppConfig;
import com.benmohammad.rxsmoke.constants.AppConstants;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;


import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class ErrorUtils {

    public static Pair<Integer, String> errorMessage(Throwable throwable, Moshi moshi) {
        String message = "Network failed!!";
        if (throwable instanceof HttpException) {
            int code = ((HttpException) throwable).code();
            switch (code) {
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    message = "Authorization error";
                    break;
                case HttpURLConnection.HTTP_UNAVAILABLE:
                    message = "Server is down you can wait or try again";
                    break;
                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                    message = "Server is down, you can wait or try again";
                    break;
                case HttpURLConnection.HTTP_FORBIDDEN:
                    message = "Access denied!";
                    break;
                case HttpURLConnection.HTTP_BAD_GATEWAY:
                    message = "Access denied!";
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    message = "Server is down try agin later";
                    break;
                case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
                    message = "Access denied";
                    break;
                default:
                    ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
                    message = parseErrorMessage(responseBody.toString(), moshi);
                    break;
            }
            return new Pair<>(code, message);
        } else if (throwable instanceof SocketTimeoutException) {
            return new Pair<>(AppConstants.SOCKET_TIME_OUT, message);
        } else if (throwable instanceof IOException) {
            return new Pair<>(AppConstants.IO_EXCEPTION, message);
        } else {
            return new Pair<>(AppConstants.UNKNOWN, message);
        }
    }

    private static String parseErrorMessage(String error, Moshi moshi) {
        String errorMessage = "";
        try {
            if (error != null) {
                JsonAdapter<Error> jsonAdapter = moshi.adapter(Error.class);
                Error errorResponse = jsonAdapter.fromJson(error);
                errorMessage = errorResponse.getMessage();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return errorMessage;
    }
}
