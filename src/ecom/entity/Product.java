package ecom.entity;

public class Product {
	private int productID;
	private String name;
	private double price;
	private String description;
	private int stockQuantity;
	
    // Constructor without productId for creating a new product
    public Product(String name, double price, String description, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.stockQuantity = stockQuantity;
    }

    // Constructor with productId for retrieving from the database
    public Product(int productId, String name, double price, String description, int stockQuantity) {
        this.productID = productId;
        this.name = name;
        this.price = price;
        this.description = description;
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

	public String getDescription() {
		return description;
	}

	public void setDescrption(String descrption) {
		this.description = descrption;
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
		return "productID=" + productID + ", name=" + name + ", price=" + price + ", descrption=" + description
				+ ", stockQuantity=" + stockQuantity;
	}
	
}
