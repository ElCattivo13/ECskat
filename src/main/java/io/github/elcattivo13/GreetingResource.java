package io.github.elcattivo13;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.stream.Stream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

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
            someStreamData().forEach(i -> {
				try {
					writer.write(i + " ");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
            writer.flush();
        };
        return Response.ok(stream).build();
    }
    
    private Stream<Integer> someStreamData() {
        return Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    }
}