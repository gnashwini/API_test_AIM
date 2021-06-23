package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class CreateUpdateReadDeleteSimulation extends Simulation
{
  val httpconfig = http.baseUrl(url="https://1ryu4whyek.execute-api.us-west-2.amazonaws.com/dev/skus")
    .header(name="Accept", value="application/json")
    .header(name="content-type", value="application/x-amz-json-1.0")

  /*createUpdate json file is created under src/test/resources/bodies folder and
  those values are being passed to post request
  createUpdate.json file has below info:
  {
 "sku":"berliner",
 "description": "Jelly donut",
 "price":"2.99"
  } */

  val scn = scenario(scenarioName = "createUpdate")
    .exec(http(requestName = "Create and Update request")
      .post(url="/")
      .body(RawFileBody("./src/test/resources/bodies/createUpdate.json")).asJson
      .header(name="content-type", value="application/json")
      .check(status is 200))
    .pause(duration = 3)

      .exec(http(requestName = "read request1")
      .get("/")
      .check(status is 200)
    .check(jsonPath(path = "$[0].sku").saveAs("skuId")))

    .exec(http(requestName = "read request2")
      .get("/${skuId}")
      .check(jsonPath("$..sku").is(expected="6d707fce-1fcd-49e0-9f64-8cdcb8d68538"))
      .check(jsonPath("$..price").is(expected = "9.99")))

    .pause(duration = 3)

      .exec(http(requestName = "Delete request")
      .delete("/${skuId}")
      .check(status in(expected =200 to 204)))
    .pause(duration = 3)


  setUp(scn.inject(atOnceUsers(users=1))).protocols(httpconfig)

}