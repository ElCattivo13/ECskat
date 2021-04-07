


@Path("table")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
public class TableResource {
    
    @Inject
    private TableBean tableBean;
    
    // PUT -> create or replace singular resource
    // POST -> add resource to collection
    // PATCH -> update singular resource
    
    
    @GET
    @Path("/findAll")
    public TableResponse findAllTables() {
        return TableResponse
            .ok()
            .setTables(tableBean.findAllTables());
    }
    
    
    @PUT
    @Path("join/{tableId}")
    public TableResponse joinTable(@CookieParam(PlayerResource.USER_ID) String userId, @PathParam String tableId) {
        try {
            tableBean.joinTable(userId, tableId);
            return TableResponse.ok();
        } catch(EcSkatException e) {
            return TableResponse.fail(e);
            
        }
    }
    
    @POST
    @Path("create/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    public TableResponse createTable(@CookieParam(PlayerResource.USER_ID) String userId, @PathParam String name, TableSettings settings) {
        try {
            tableBean.createTable(name, userId, settings);
            return TableResponse.ok();
        } catch (UnknownPlayerException e) {
            return TableResponse.fail(e);
        }
    }
    
    @PUT
    @Path("startGame/{tableId}")
    public TableResponse startGame(@CookieParam(PlayerResource.USER_ID) String userId, @PathParam("tableId") String tableId) {
        try {
            tableBean.austeilen(userId, tableId);
            return tableBean.ok();
        } catch(EcSkatException e) {
            return TableResponse.fail(e);
        }
    }
    
    @PUT
    @Path("sagen/{tableId}/{reizwert}")
    public TableResponse sagen(@CookieParam(PlayerResource.USER_ID) String userId, @PathParam("tableId") String tableId, @PathParam("reizwert") Integer reizwert) {
        try {
            tableBean.sagen(userId, tableId, reizwert);
            return TableResponse.ok();
        } catch(EcSkatException e) {
            return TableResponse.fail(e);
        }
    }
    
    @PUT
    @Path("hoeren/{tableId}/yes")
    public TableResponse hoerenJa(@CookieParam(PlayerResource.USER_ID) String userId, @PathParam("tableId") String tableId) {
        return hoeren(userId, tableId, true);
    }
    
    @PUT
    @Path("hoeren/{tableId}/no")
    public TableResponse hoerenJa(@CookieParam(PlayerResource.USER_ID) String userId, @PathParam("tableId") String tableId) {
        return hoeren(userId, tableId, false);
    }
    
    private TableResponse hoeren(String userId, String tableId, boolean ja) {
        try {
            tableBean.hoeren(userId, tableId, false);
            return TableResponse.ok();
        } catch(EcSkatException e) {
            return TableResponse.fail(e);
        }
    }
    
    @PUT
    @Path("skataufnehmen/{tableId}")
    public TableResponse skatAufnehmen(@CookieParam(PlayerResource.USER_ID) String userId, @PathParam("tableId") String tableId) {
        try {
            tableBean.skatAufnehmen(userId, tableId);
            return TableResponse.ok();
        } catch(EcSkatException e) {
            return TableResource.fail(e);
        }
    }
    
    
}