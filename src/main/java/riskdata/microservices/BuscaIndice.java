package riskdata.microservices;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import edu.asu.lucene.service.rest.exception.InvalidLuceneQueryException;
import edu.asu.lucene.service.rest.exception.LuceneSearcherException;
import edu.asu.lucene.service.rest.search.LuceneSearcher;
import edu.asu.lucene.service.rest.search.Result;

@Component
public class BuscaIndice {
	@Autowired
	private LuceneSearcher indexSearcher;

	@Value("${lucene.query.default.records}")
	private Integer QUERY_DEFAULT_RECORDS;

	@Value("${lucene.query.max.records}")
	private Integer QUERY_MAX_RECORDS;
	
	private final static Logger logger = Logger.getLogger("IndiceServiceController");

	public Result buscarPorRazaoSocial(String razaoSocial, boolean buscaExata)
			throws LuceneSearcherException, InvalidLuceneQueryException {
		if(razaoSocial.trim().equals("")) {
			return null;
		}
		
		String query = "razao_social:";
		if (buscaExata) {
			query += "\"" + razaoSocial + "\"";
		} else {
			razaoSocial = razaoSocial.replace("/", "\\/");
			query += razaoSocial;
		}

		int count = QUERY_DEFAULT_RECORDS;
		boolean showAvailable = false;

		Result results = indexSearcher.searchIndex(query, count, showAvailable);
		logger.info("Search for '" + query + "' found " + results.getAvailable() + " and retrieved "
				+ results.getRetrieved() + " records");
		return results;
	}

}
