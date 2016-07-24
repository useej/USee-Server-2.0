package com.usee.dao;

import com.usee.model.User;

public interface CellphoneValidateDao {
	
	public User getUserByCellphone(String cellphone);
	
	public boolean updateValidateCode(User user);
	
	public void saveValidateCode(User user);

}
