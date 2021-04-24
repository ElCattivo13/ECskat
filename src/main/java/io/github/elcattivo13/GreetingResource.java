package io.github.elcattivo13;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello-resteasy")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello RESTEasy!!!";
    }
    
    @GET
    public Response streamExample(){
        StreamingOutput stream = out -> {
            Writer writer = new BufferedWriter(new OutputStreamWriter(out));
            someStreamData().forEach(i -> writer.write(i + " "));
            writer.flush();
        };
        return Response.ok(stream).build();
    }
    
    private Stream<Integer> someStreamData() {
        return Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    }
}