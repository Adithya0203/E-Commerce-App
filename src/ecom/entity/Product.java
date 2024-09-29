package ecom.entity;

public class Product {
	private int productID;
	private String name;
	private double price;
	private String descrption;
	private int stockQuantity;
	
//	constructor
	
	public Product(int productID, String name, double price, String descrption, int stockQuantity) {
		super();
		this.productID = productID;
		this.name = name;
		this.price = price;
		this.descrption = descrption;
		this.stockQuantity = stockQuantity;
	}
	
//	getters and setters

	public int getProductID() {
		return productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescrption() {
		return descrption;
	}

	public void setDescrption(String descrption) {
		this.descrption = descrption;
	}

	public int getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

//	toString
	
	@Override
	public String toString() {
		return "Product [productID=" + productID + ", name=" + name + ", price=" + price + ", descrption=" + descrption
				+ ", stockQuantity=" + stockQuantity + "]";
	}
	
}
