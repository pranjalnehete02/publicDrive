package com.example.security;

public class JwtResponse {
    private String jwtToken;
    private String email;
    private String roleName;
    private int userId;  // Add userId to the response

    public JwtResponse(String jwtToken, String email, String roleName, int userId) {
        this.jwtToken = jwtToken;
        this.email = email;
        this.roleName = roleName;
        this.userId = userId;
    }

    // Getters and setters
    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

 

    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

   
}
