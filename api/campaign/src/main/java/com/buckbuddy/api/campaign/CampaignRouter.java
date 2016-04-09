package com.buckbuddy.api.campaign;

import static spark.Spark.*;

public class CampaignRouter {

  public static void main(String[] args) {
    get("/hello", (req, res) -> {
      return "Hello World!";
    });
  }

}