package api.model;

import lombok.Data;

@Data
public class TinyBoom_Login extends BaseModel {
	private String passwd;
	private String username;
}