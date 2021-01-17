/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package errorhandling;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 *
 * @author marcg
 */
public class DateExceptionMapper implements ExceptionMapper<DateException> {

    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public Response toResponse(DateException ex) {
        Logger.getLogger(DateExceptionMapper.class.getName())
                .log(Level.SEVERE, null, ex);
        ExceptionDTO err = new ExceptionDTO(400, ex.getMessage());
        return Response
                .status(400)
                .entity(gson.toJson(err))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
