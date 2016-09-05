package com.lenkp.asteriskmonitor.dao;

import java.util.List;

public interface Dao {

	/**
	 * @return all item from database
	 */
	public List<Dao> getAll();
	
	/**
	 * Update an item in database
	 * 
	 * @param object
	 */
	public void update(Object object);
	
	/**
	 * Delete an item in database
	 * 
	 * @param object
	 */
	public void delete(Object object);
	
	/**
	 * Add an item to database
	 * 
	 * @param object
	 */
	public void add(Object object);
	
}
