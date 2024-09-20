package com.zc.cryptohelper.crypto_helper.storage.exceptions;

public class UploadException extends RuntimeException {

  public UploadException(String message) {
    super(message);
  }

  public UploadException(String message, Throwable cause) {
    super(message, cause);
  }
}
