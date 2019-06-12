package coupons.CouponsSystemWeb.entities;

/**
 * 
 * Represents a client user in the system
 *
 */
public class User {

	private String name, password;

	private ClientType clientType;

	/**
	 * Constructor for User
	 * 
	 * @param name       Name of user
	 * @param password   Password of user
	 * @param clientType Client Type of user
	 */
	public User(String name, String password, ClientType clientType) {
		this.name = name;
		this.password = password;
		this.clientType = clientType;
	}

	/**
	 * Returns name of user
	 * 
	 * @return name Name of user
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets name of user
	 * 
	 * @param name Name of user
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns password of user
	 * 
	 * @return name Password of user
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets password of user
	 * 
	 * @param password Password of user
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Returns client type of user
	 * 
	 * @return clientType Client Type of user
	 */
	public ClientType getClientType() {
		return clientType;
	}

	/**
	 * Sets client type of user
	 * 
	 * @param clientType Client Type of user
	 */
	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
	}

}
