package com.agora.agora;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.agora.agora.model.Identifiable;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AgoraApplication.class)
@ActiveProfiles("test")
@WebAppConfiguration
public abstract class AbstractTest {
    MockMvc mvc;
    protected ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Before
    public void setUpMvcAndMapper() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(SecurityMockMvcConfigurers.springSecurity()).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    String mapToJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    String fullMapToJson(Object obj) throws JsonProcessingException {
        objectMapper.disable(MapperFeature.USE_ANNOTATIONS);
        String result = objectMapper.writeValueAsString(obj);
        objectMapper.enable(MapperFeature.USE_ANNOTATIONS);
        return result;
    }

    <T> T mapFromJson(String json, Class<T> clazz) throws IOException {
        return objectMapper.readValue(json, clazz);
    }

    <T> T mapFromJson(String json, TypeReference type) throws IOException {
        return objectMapper.readValue(json, type);
    }

    void setAuthentication(String username, String password, String role) {
        Collection<? extends GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
        User principal = new User(username, password, authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    <T extends Identifiable, S extends Identifiable> boolean compareIdentifiableLists(List<T> expected, List<S> actual) {
        if (expected.size() != actual.size()) return false;
        else if (actual.size() == 0) return true;
        boolean result = false;
		for (Identifiable identifiable1 : expected) {
			for (Identifiable identifiable2 : actual) {
				if(identifiable1.getId() == identifiable2.getId()) {
					result = true;
					break;
				} else {
					result = false;
				}
			}
			if(!result) {
			    return false;
			}
		}
		return true;
    }
}
