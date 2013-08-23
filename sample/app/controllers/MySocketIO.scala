package controllers


import play.api.libs.json._


import PacketTypes._

object MySocketIOController extends SocketIOController {

  def processMessage(sessionId: String, packet: Packet) {

    packet.packetType match {
      //Process regular message
      case (MESSAGE) => {
        println("Processed request for sessionId: " + packet.data)
        //DO your message processing here
        Enqueue("Processed request for sessionId: " + packet.data)
      }

      //Process JSON message
      case (JSON) => {
        println("Processed request for sessionId: " + packet.data)
        //
        Enqueue("Processed request for sessionId: " + packet.data)
      }

      //Handle event
      case (EVENT) => {
        println("Processed request for sessionId: " + packet.data)
        // "Processed request for sessionId: " + eventData
        val parsedData: JsValue = Json.parse(packet.data)
        (parsedData \ "name").asOpt[String] match {
          case Some("my other event") => {
            val data = parsedData \ "args"
            //process data
            println( """"my other event" just happened for client: """ + sessionId + " data sent: " + data)
            enqueueJsonMsg(sessionId, "Processed request for sessionId: " + data)
          }
          case _ =>
            println("Unkown event happened.")
        }
      }

    }

  }
}
