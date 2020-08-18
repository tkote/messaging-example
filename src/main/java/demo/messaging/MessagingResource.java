
package demo.messaging;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/")
@ApplicationScoped
public class MessagingResource {
    private final static Logger logger = Logger.getLogger(MsgProcessingBean.class.getName());

    @Inject MsgProcessingBean processor;

    @GET @Path("/submit")
    public void submit(@QueryParam("key") String key, @QueryParam("value") String value) {
        logger.info(String.format("@GET /submit: key=%s, value=%s", key, value));
        processor.submit(new Message(key, value));
    }

    @POST @Path("/submit")
    @Consumes(MediaType.APPLICATION_JSON)
    public void submit(Message message) {
        logger.info(String.format("@POST /submit: key=%s, value=%s", message.getKey(), message.getValue()));
        processor.submit(message);
    }

}
