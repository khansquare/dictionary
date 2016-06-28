package com.square.dictionary.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.square.dictionary.model.Word;
import com.square.dictionary.util.Log;

@Service("WordService")
@Transactional
public class WordServiceImpl implements WordService {

	public Word findWordById(String id) {
		Session session = null;
		Word word = new Word();
		try {
			session = getSession();
			session.beginTransaction();
			Query procedure = session.createSQLQuery("call get_details_by_id (:id)")
					.setParameter("id", id);
			procedure.setResultTransformer(Transformers.aliasToBean(Word.class));		
			word = (Word) procedure.uniqueResult();			
			session.getSessionFactory().close();
		} catch (JDBCConnectionException e) {			
			Log.e(WordServiceImpl.class, e);
		} catch (IOException e) {
			Log.e(WordServiceImpl.class, e);
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}		
		return word;
	}

	public Word findWordByName(String name) {
		Session session = null;
		Word word = new Word();
		try {
			session = getSession();
			session.beginTransaction();
			Query procedure = session.createSQLQuery("call get_details_by_name (:name)")
					.setParameter("name", name);
			procedure.setResultTransformer(Transformers.aliasToBean(Word.class));		
			word = (Word) procedure.uniqueResult();			
			session.getSessionFactory().close();
		} catch (JDBCConnectionException e) {			
			Log.e(WordServiceImpl.class, e);
		} catch (IOException e) {
			Log.e(WordServiceImpl.class, e);
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}		
		return word;
	}

	@SuppressWarnings("unchecked")
	public List<Word> findWordsByCategory(String category) {		
		Session session = null;
		List<Word> words = new ArrayList<Word>();
		try {
			session = getSession();
			session.beginTransaction();
			Query procedure = session.createSQLQuery("call get_words_by_category (:category)")
					.setParameter("category", category);				
			procedure.setResultTransformer(Transformers.aliasToBean(Word.class));		
			words = procedure.list();			
			session.getSessionFactory().close();
		} catch (JDBCConnectionException e) {			
			Log.e(WordServiceImpl.class, e);
		} catch (IOException e) {
			Log.e(WordServiceImpl.class, e);
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}		
		return words;
	}

	public static Session getSession() throws JDBCConnectionException, IOException {
		Log.i(WordServiceImpl.class, "hibernate.session.opening");
		Properties properties = new Properties();		
		properties.load(WordServiceImpl.class.getClassLoader().getResourceAsStream("/config.properties"));
		Configuration configuration = new Configuration().setProperties(properties);
		return configuration.buildSessionFactory(new StandardServiceRegistryBuilder().
				applySettings(configuration.getProperties()).build()).openSession();		 
	}	
}