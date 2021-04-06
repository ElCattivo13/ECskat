import io.github.elcattivo13.pojos.Player;

public class PlayerResponse {
    private final String status;
    private List<Player> players;
    
    
    private PlayerResponse(String status) {
        this.status = status;
    }
    
    public static PlayerResponse ok() {
        return new PlayerResponse("OK");
    }
    
    public static PlayerResponse fail(EcSkatException e) {
        return new PlayerResponse(e.getReason().toString());
    }
    
    
    
    public Response toResponse() {
        return toResponse(null);
    }
    
    public Response toResponse(NewCookie cookie) {
        ResponseBuilder builder = Response.ok()
            .entity(this)
            .type(MediaType.APPLICATION_JSON);
            
        if (cookie != null) {
            builder = builder.cookie(cookie);
        }
        
        return builder.build();
    }
    
    
    public List<Player> getPlayers(){
        return this.players;
    }
        
    public PlayerResponse setPlayers(List<Player> players){
        this.players = players;
        return this;
    }
    
}