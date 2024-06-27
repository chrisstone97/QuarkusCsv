package com.rc;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Produces(MediaType.TEXT_HTML)
@Path("/index.html")
public class ReadController {

    @Location("index.html")
    Template template;

    @Inject
    EntityManager em;


    @GET
    public TemplateInstance get() {
        return template.instance();
    }

    @GET
    @RolesAllowed("admin")
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/admin")
    public void getAsAdmin() {
        System.out.println("admin");
    }

    @GET
    @RolesAllowed("user")
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/user")
    public void getAsAll() {
        System.out.println("user non admin");
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @PermitAll
    @Transactional
    public TemplateInstance upload(FileUploadInput input) throws IOException {

        for (FileUpload file : input.file) {
            System.out.println("filename = " + file.fileName());
            List<User> userList = new BufferedReader(new FileReader(file.uploadedFile().toFile()))
                    .lines()
                    .skip(1) //Skips the first n lines, in this case 1
                    .map(s -> {
                        //csv line parsing and xml logic here
                        //...
                        User user = new User();
                        if (s != null) {
                            String[] listUser = s.split(";");
                            System.out.println(Arrays.toString(listUser));
                            user.setUsername(listUser[0]);
                            user.setIdentifier(listUser[1]);
                            user.setFirstName(listUser[2]);
                            user.setLastName(listUser[3]);
                        }
                        return user;
                    })
                    .collect(Collectors.toList());

            for (User s : userList) {
                System.out.println(s.getUsername());
                em.persist(s);
            }
        }

        return template.instance();
    }

    public static class FileUploadInput {

        @FormParam("text")
        public String text;

        @FormParam("file")
        public List<FileUpload> file;

    }
}
