package com.allen.demoserver.deserializer;

import com.allen.demoserver.entity.CustomUserDetails;
import com.allen.demoserver.entity.SysUser;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.IOException;
import java.util.List;

/**
 * @Author xuguocai
 * @Date 14:07 2022/12/27
 *
 * https://www.pudn.com/news/62a9565ca11cf7345fa02006.html
 **/
public class CustomUserDetailsDeserializer extends JsonDeserializer<CustomUserDetails> {

	private static final TypeReference<List<GrantedAuthority>> SIMPLE_GRANTED_AUTHORITY_SET = new TypeReference<>() {
	};

	/**
	 * This method will create {@link User} object. It will ensure successful object
	 * creation even if password key is null in serialized json, because credentials
	 * may be removed from the {@link User} by invoking
	 * {@link User#eraseCredentials()}. In that case there won't be any password key
	 * in serialized json.
	 *
	 * @param jp   the JsonParser
	 * @param ctxt the DeserializationContext
	 * @return the user
	 * @throws IOException             if a exception during IO occurs
	 * @throws JsonProcessingException if an error during JSON processing occurs
	 */
	@Override
	public CustomUserDetails deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ObjectMapper mapper = (ObjectMapper) jp.getCodec();
		JsonNode jsonNode = mapper.readTree(jp);

//		Set<? extends GrantedAuthority> authorities = (Set)mapper.convertValue(jsonNode.get("authorities"), SIMPLE_GRANTED_AUTHORITY_SET);
		List<String> roles = mapper.convertValue(jsonNode.get("roles"), List.class);

		JsonNode passwordNode = readJsonNode(jsonNode, "password");
		String username = readJsonNode(jsonNode, "username").asText();
		String nickName = readJsonNode(jsonNode, "nickName").asText();
		String password = passwordNode.asText("");
		String email = readJsonNode(jsonNode, "email").asText();
		String id = readJsonNode(jsonNode, "id").asText();

		boolean enabled = readJsonNode(jsonNode, "enabled").asBoolean();
		boolean accountNonExpired = readJsonNode(jsonNode, "accountNonExpired").asBoolean();
		boolean credentialsNonExpired = readJsonNode(jsonNode, "credentialsNonExpired").asBoolean();
		boolean accountNonLocked = readJsonNode(jsonNode, "accountNonLocked").asBoolean();

		SysUser details = new SysUser(id,username,nickName,email,password,roles);

//		User result = new User(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked,
//			authorities);

		return new CustomUserDetails(details);
	}

	private JsonNode readJsonNode(JsonNode jsonNode, String field) {
		return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
	}

}
