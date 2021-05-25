/*
 * Copyright 2015-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rongji.egov.security.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.rongji.egov.security.SecurityUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Set;

/**
 * @author daihuabin
 */
public class SecurityUserDeserializer extends JsonDeserializer<SecurityUser> {

    @Override
    public SecurityUser deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);
        Set<SimpleGrantedAuthority> authorities = mapper.convertValue(jsonNode.get("authorities"), new TypeReference<Set<SimpleGrantedAuthority>>() {
        });
        JsonNode password = readJsonNode(jsonNode, "password");

        SecurityUser result = new SecurityUser();
        result.setUsername(readJsonNode(jsonNode, "username").asText());
        result.setPassword(password.asText(""));
        result.setEnabled(readJsonNode(jsonNode, "enabled").asBoolean());
        result.setAccountNonExpired(readJsonNode(jsonNode, "accountNonExpired").asBoolean());
        result.setCredentialsNonExpired(readJsonNode(jsonNode, "credentialsNonExpired").asBoolean());
        result.setAccountNonLocked(readJsonNode(jsonNode, "accountNonLocked").asBoolean());
        result.setSystemNo(readJsonNode(jsonNode, "systemNo").asText());
        result.setOrgNo(readJsonNode(jsonNode, "orgNo").asText());
        result.setAuthorities(authorities);
        return result;
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }
}
