package utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.IdGenerator.Prefix;
import utils.IdManager.IdType;

import mail.Mailer;
import models.PersistentId;
import models.dao.DaoProvider;
import birt.ReportEngine;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.avaje.ebean.Transaction;
import com.google.inject.Inject;

public class PersistentIdManager implements IdManager{
	private static final Logger LOG = LoggerFactory.getLogger(PersistentIdManager.class);
	private final IdGenerator idGenerator;
	
	@Inject
    public PersistentIdManager(IdGenerator idGenerator) {
    	this.idGenerator = idGenerator;
    }
	public Long generateReceiptNumber(IdType idType) {
		return generateKeyValue(idType);
	}
	
	public String generateFormNumber(IdType idType, Prefix prefix) {
		  return idGenerator.generate(generateKeyValue(idType), prefix);
	}
	
	private Long generateKeyValue(IdType name) {
		  Transaction transaction = Ebean.beginTransaction();
	      Long keyValue = null;
	      try {
	  		  Query<PersistentId> query = Ebean.createQuery(PersistentId.class); 
	  		  query.where().eq("name", name.getName());
	  		  query.setForUpdate(true);
	  		  PersistentId persistentId = query.findUnique();
	  		  if(persistentId != null){
	  			keyValue = persistentId.keyValue;
		  		persistentId.keyValue = keyValue + 1;
		  		Ebean.update(persistentId);
	  		  }
	  		  transaction.commit();
	      }catch(Exception t){
	    	  LOG.info("Error occured while while getting key value " + t);
	    	  transaction.rollback();
	      }finally {
	    	  transaction.end();
	      }
	      
	      if(keyValue == null){
	    	  throw new IllegalStateException("No key value available for" + name.getName());
	      }
	    	  return keyValue;
	}
}
