package com.zc.cryptohelper.crypto_helper.storage.exceptions;

public class UploadFileNotFoundException extends UploadException {

  public UploadFileNotFoundException(String message) {
    super(message);
  }

  public UploadFileNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
