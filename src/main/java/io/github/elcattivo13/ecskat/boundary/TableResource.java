package io.github.elcattivo13.ecskat.boundary;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import io.github.elcattivo13.ecskat.beans.TableBean;
import io.github.elcattivo13.ecskat.errorhandling.EcSkatException;
import io.github.elcattivo13.ecskat.pojos.TableSettings;

@Path("table")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
public class TableResource {
    
    @Inject
    TableBean tableBean;
    
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
    @Path("join")
    public TableResponse joinTable(@CookieParam(PlayerResource.USER_ID) String userId, @QueryParam("tableId") String tableId) {
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
    public TableResponse createTable(@CookieParam(PlayerResource.USER_ID) String userId, @PathParam("name") String name, TableSettings settings) {
        try {
            String tableId = tableBean.createTable(name, userId, settings);
            return TableResponse.ok().setTableId(tableId);
        } catch (EcSkatException e) {
            return TableResponse.fail(e);
        }
    }
    
    @PUT
    @Path("startGame")
    public TableResponse startGame(@CookieParam(PlayerResource.USER_ID) String userId, @QueryParam("tableId") String tableId) {
        try {
            tableBean.austeilen(userId, tableId);
            return TableResponse.ok();
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
    public TableResponse hoerenWeg(@CookieParam(PlayerResource.USER_ID) String userId, @PathParam("tableId") String tableId) {
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
            return TableResponse.fail(e);
        }
    }
    
    @PUT
    @Path("kontrasagen/{tableId}")
    public TableResponse kontraSagen(@CookieParam(PlayerResource.USER_ID) String userId, @PathParam("tableId") String tableId) {
        try {
            tableBean.kontraSagen(userId, tableId);
            return TableResponse.ok();
        } catch(EcSkatException e) {
            return TableResponse.fail(e);
        }
    }
    
    @PUT
    @Path("resagen/{tableId}")
    public TableResponse reSagen(@CookieParam(PlayerResource.USER_ID) String userId, @PathParam("tableId") String tableId) {
        try {
            tableBean.reSagen(userId, tableId);
            return TableResponse.ok();
        } catch(EcSkatException e) {
            return TableResponse.fail(e);
        }
    }
}