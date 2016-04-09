package com.buckbuddy.api.donation;

import static spark.Spark.*;

public class DonationRouter {

  public static void main(String[] args) {
    get("/hello", (req, res) -> {
      return "Hello World!";
    });
  }

}