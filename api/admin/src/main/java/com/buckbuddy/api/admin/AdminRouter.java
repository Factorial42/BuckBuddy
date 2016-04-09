package com.buckbuddy.api.admin;

import static spark.Spark.*;

public class AdminRouter {

  public static void main(String[] args) {
    get("/hello", (req, res) -> {
      return "Hello World!";
    });
  }

}