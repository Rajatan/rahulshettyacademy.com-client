package rahulShettyAcademy;

public class addToCartPayload {
	
	private String _id;
	private ProductAddToCartPayload product;
	
	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public ProductAddToCartPayload getProduct() {
		return product;
	}

	public void setProduct(ProductAddToCartPayload productPayload) {
		this.product = productPayload;
	}

}
