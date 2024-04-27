import React from 'react';
import "./productsDetails.css";



const ProductDetails = ({ name, price, quantity, total,src }
    :{name:string,price:number,quantity:number,total:number,src:string}

) => {
  return (
    <div className='product'>
        <div className='detail'>
        <div className='detail-img'>
            <img className='img' src={`../${src}`}/>
        </div>
        </div>
    <div className="product-details">
        
      <div className="detail">
        <span className="value">{name}:</span>
      </div>
      <div className="detail">
        <span className="value-price">{price}€</span>
      </div>
      <div className='datail'>
        <span className="label-x">X</span>
      </div>
      <div className="detail">
        <span className="value">{quantity}</span>
      </div>
    </div>
    <div className="detail-unit">
        <span className="label">Total:</span>
        <span className="value-total">{total}€</span>
      </div>
    </div>
  );
}

export default ProductDetails;
