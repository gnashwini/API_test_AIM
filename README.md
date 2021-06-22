# API_test_AIM

package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class CreateUpdateReadDeleteSimulation extends Simulation
{
  val httpconfig = http.baseUrl(url="https://1ryu4whyek.execute-api.us-west-2.amazonaws.com/dev/skus")
    .header(name="Accept", value="application/json")
    .header(name="content-type", value="application/json")

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

      .exec(http(requestName = "read request")
      .get("/")
      .check(status is 200))
    .pause(duration = 3)

      .exec(http(requestName = "Delete request")
      .delete("/")
      .check(status in(expected =200 to 204)))
    .pause(duration = 3)


  setUp(scn.inject(atOnceUsers(users=1))).protocols(httpconfig)

}
