import io.github.elcattivo13.pojos.TableSettings;

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
    public TableResponse createTable(@CookiParam(PlayerResource.USER_ID) String userId, @PathParam String name, TableSettings settings) {
        try {
            tableBean.createTable(name, userId, settings);
            return TableResponse.ok();
        } catch (UnknownPlayerException e) {
            return TableResponse.fail(e);
        }
    }
    
    
}