package io.github.elcattivo13.ecskat.boundary;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import io.github.elcattivo13.ecskat.beans.PlayerBean;
import io.github.elcattivo13.ecskat.errorhandling.EcSkatException;
import io.github.elcattivo13.ecskat.pojos.Player;

@Path("player")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
public class PlayerResource {
    
    public static final String USER_ID = "userId";
    
    @Inject
    PlayerBean playerBean;
    
    @GET
    @Path("/findAll")
    public PlayerResponse findAllPlayers() {
        return PlayerResponse
            .ok()
            .setPlayers(playerBean.findAllPlayers());
    }
    
    @PUT
    @Path("/{name}")
    public Response createOrUpdatePlayer(@CookieParam(USER_ID) Cookie userIdCookie, @PathParam("name") String name) {
        if (userIdCookie == null) {
            String playerId = playerBean.createPlayer(name);
            return PlayerResponse.ok().toResponse(new NewCookie(USER_ID, playerId, "/ecskat/", null, "toll", 60*60*24, false));
        } else {
            try {
                Player player = playerBean.findPlayer(userIdCookie.getValue());
                player.setName(name);
                // TODO broadcast changes via websocket
                return PlayerResponse.ok().toResponse();
            } catch(EcSkatException e) {
                return PlayerResponse.fail(e).toResponse(new NewCookie(userIdCookie, null, 0, false));
            }
        }
    }
    
    @PUT
    @Path("/ready")
    public PlayerResponse ready(@CookieParam(USER_ID) String userId) {
        try {
            playerBean.toggleReady(userId, true);
            return PlayerResponse.ok();
        } catch(EcSkatException e) {
            return PlayerResponse.fail(e);
        }
        
    }
    
    @PUT
    @Path("/notready")
    public PlayerResponse notReady(@CookieParam(USER_ID) String userId) {
        try {
            playerBean.toggleReady(userId, false);
            return PlayerResponse.ok();
        } catch(EcSkatException e) {
            return PlayerResponse.fail(e);
        }
    }
    
    @PUT
    @Path("/cutposition/{pos}")
    public PlayerResponse putCutposition(@CookieParam(USER_ID) String userId, @PathParam("pos") CutPostion pos) {
        try {
            playerBean.setCutPosition(userId, pos);
            return PlayerResponse.ok();
        } catch(EcSkatException e) {
            return PlayerResponse.fail(e);
        }
    }
    
}