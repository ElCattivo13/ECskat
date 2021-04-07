


@Path("player")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
public class PlayerResource {
    
    public static final String USER_ID = "userId";
    
    @Inject
    private PlayerBean playerBean;
    
    @GET
    @Path("/findAll")
    public PlayerResponse findAllPlayers() {
        return PlayerResponse
            .ok()
            .setPlayers(playerBean.findAllPlayers());
    }
    
    @PUT
    @Path("/{name}")
    public Response createOrUpdatePlayer(@CookieParam(USER_ID) Cookie userId, @PathParam String name) {
        if (cookie == null) {
            String playerId = playerBean.createPlayer(name);
            return PlayerResponse.ok().toResponse(new NewCookie(USER_ID, playerId));
        } else {
            try {
                Player player = playerBean.findPlayer(cookie.getValue());
                player.setName(name);
                // TODO broadcast changes via websocket
                return PlayerResponse..ok().toResponse();
            } catch(UnknownPlayerException e) {
                return PlayerResponse.fail(e).toResponse(new NewCookie(cookie, null, 0, false));
            }
        }
    }
    
    @PUT
    @Path("/ready")
    public PlayerResponse ready(@CookieParam(USER_ID) Strig userId) {
        try {
            playerBean.toggleReady(userId, true);
            return PlayerResponse.ok();
        } catch(UnknownPlayerException e) {
            return PlayerResponse.fail(e);
        }
        
    }
    
    @PUT
    @Path("/notready")
    public PlayerResponse notReady(@CookieParam(USER_ID) String userId) {
        try {
            playerBean.toggleReady(userId, false);
            return PlayerResponse.ok();
        } catch(UnknownPlayerException e) {
            return PlayerResponse.fail(e);
        }
    }
}