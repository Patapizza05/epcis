package org.oliot.epcis.service.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.oliot.epcis.configuration.Configuration;
import org.oliot.epcis.security.OAuthUtil;
import org.oliot.epcis.service.query.mongodb.MongoQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import com.restfb.Connection;
import com.restfb.FacebookClient;
import com.restfb.types.User;

/**
 * Copyright (C) 2014 Jaewook Byun
 *
 * This project is part of Oliot (oliot.org), pursuing the implementation of
 * Electronic Product Code Information Service(EPCIS) v1.1 specification in
 * EPCglobal.
 * [http://www.gs1.org/gsmp/kc/epcglobal/epcis/epcis_1_1-standard-20140520.pdf]
 * 
 *
 * @author Jaewook Byun, Ph.D student
 * 
 *         Korea Advanced Institute of Science and Technology (KAIST)
 * 
 *         Real-time Embedded System Laboratory(RESL)
 * 
 *         bjw0829@kaist.ac.kr, bjw0829@gmail.com
 */

@Controller
public class RESTLikeQueryService implements ServletContextAware {

	@Autowired
	ServletContext servletContext;

	@SuppressWarnings("unused")
	@Autowired
	private HttpServletRequest request;

	@SuppressWarnings("unused")
	@Autowired
	private HttpServletResponse response;

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Deprecated
	@RequestMapping(value = "/Subscribe/{queryName}/{subscriptionID}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> subscribe(@PathVariable String queryName, @PathVariable String subscriptionID,
			@RequestParam String dest, @RequestParam String cronExpression, @RequestParam Map<String, String> params) {
		Configuration.logger.error("epcis-query service does not provide this API");
		return null;
	}

	@Deprecated
	@RequestMapping(value = "/Unsubscribe/{subscriptionID}", method = RequestMethod.GET)
	public ResponseEntity<?> unsubscribe(@PathVariable String subscriptionID) {
		Configuration.logger.error("epcis-query service does not provide this API");
		return null;
	}

	@Deprecated
	@RequestMapping(value = "/GetSubscriptionIDs/{queryName}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getSubscriptionIDsREST(@PathVariable String queryName) {
		Configuration.logger.error("epcis-query service does not provide this API");
		return null;
	}

	/**
	 * Returns a list of all query names available for use with the subscribe
	 * and poll methods. This includes all pre-defined queries provided by the
	 * implementation, including those specified in Section 8.2.7.
	 * 
	 * No Dependency with Backend
	 * 
	 * @return JSONArray of query names ( String )
	 */
	@RequestMapping(value = "/GetQueryNames", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getQueryNamesREST() {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=utf-8");

		JSONArray jsonArray = new JSONArray();
		List<String> queryNames = getQueryNames();
		for (int i = 0; i < queryNames.size(); i++) {
			jsonArray.put(queryNames.get(i));
		}

		return new ResponseEntity<>(jsonArray.toString(1), responseHeaders, HttpStatus.OK);
	}

	/**
	 * Returns a list of all query names available for use with the subscribe
	 * and poll methods. This includes all pre-defined queries provided by the
	 * implementation, including those specified in Section 8.2.7.
	 * 
	 * @return a list of all query names
	 */
	public List<String> getQueryNames() {
		List<String> queryNames = new ArrayList<String>();
		queryNames.add("SimpleEventQuery");
		queryNames.add("SimpleMasterDataQuery");
		return queryNames;
	}

	/**
	 * Returns a string that identifies what version of the specification this
	 * implementation complies with. The possible values for this string are
	 * defined by GS1. An implementation SHALL return a string corresponding to
	 * a version of this specification to which the implementation fully
	 * complies, and SHOULD return the string corresponding to the latest
	 * version to which it complies. To indicate compliance with this Version
	 * 1.2 of the EPCIS specification, the implementation SHALL return the
	 * string 1.2 .
	 * 
	 * @return 1.2
	 */
	@RequestMapping(value = "/GetStandardVersion", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getStandardVersion() {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		return new ResponseEntity<>(new String("1.2"), responseHeaders, HttpStatus.OK);
	}

	/**
	 * Returns a string that identifies what vendor extensions this
	 * implementation provides. The possible values of this string and their
	 * meanings are vendor-defined, except that the empty string SHALL indicate
	 * that the implementation implements only standard functionality with no
	 * vendor extensions. When an implementation chooses to return a non-empty
	 * string, the value returned SHALL be a URI where the vendor is the owning
	 * authority. For example, this may be an HTTP URL whose authority portion
	 * is a domain name owned by the vendor, a URN having a URN namespace
	 * identifier issued to the vendor by IANA, an OID URN whose initial path is
	 * a Private Enterprise Number assigned to the vendor, etc.
	 * 
	 * @return a string of vendor version
	 */
	@RequestMapping(value = "/GetVendorVersion", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getVendorVersion() {
		// It is not a version of Vendor
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");

		return new ResponseEntity<>(new String(), responseHeaders, HttpStatus.OK);
	}

	@RequestMapping(value = "/Poll/{queryName}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> poll(@PathVariable String queryName, @RequestParam(required = false) String eventType,
			@RequestParam(required = false) String GE_eventTime, @RequestParam(required = false) String LT_eventTime,
			@RequestParam(required = false) String GE_recordTime, @RequestParam(required = false) String LT_recordTime,
			@RequestParam(required = false) String EQ_action, @RequestParam(required = false) String EQ_bizStep,
			@RequestParam(required = false) String EQ_disposition, @RequestParam(required = false) String EQ_readPoint,
			@RequestParam(required = false) String WD_readPoint, @RequestParam(required = false) String EQ_bizLocation,
			@RequestParam(required = false) String WD_bizLocation,
			@RequestParam(required = false) String EQ_transformationID,
			@RequestParam(required = false) String MATCH_epc, @RequestParam(required = false) String MATCH_parentID,
			@RequestParam(required = false) String MATCH_inputEPC,
			@RequestParam(required = false) String MATCH_outputEPC, @RequestParam(required = false) String MATCH_anyEPC,
			@RequestParam(required = false) String MATCH_epcClass,
			@RequestParam(required = false) String MATCH_inputEPCClass,
			@RequestParam(required = false) String MATCH_outputEPCClass,
			@RequestParam(required = false) String MATCH_anyEPCClass,
			@RequestParam(required = false) Integer EQ_quantity, @RequestParam(required = false) Integer GT_quantity,
			@RequestParam(required = false) Integer GE_quantity, @RequestParam(required = false) Integer LT_quantity,
			@RequestParam(required = false) Integer LE_quantity,

			@RequestParam(required = false) String EQ_eventID,
			@RequestParam(required = false) Boolean EXISTS_errorDeclaration,
			@RequestParam(required = false) String GE_errorDeclarationTime,
			@RequestParam(required = false) String LT_errorDeclarationTime,
			@RequestParam(required = false) String EQ_errorReason,
			@RequestParam(required = false) String EQ_correctiveEventID,

			@RequestParam(required = false) String orderBy, @RequestParam(required = false) String orderDirection,
			@RequestParam(required = false) Integer eventCountLimit,
			@RequestParam(required = false) Integer maxEventCount,

			@RequestParam(required = false) String vocabularyName,
			@RequestParam(required = false) Boolean includeAttributes,
			@RequestParam(required = false) Boolean includeChildren,
			@RequestParam(required = false) String attributeNames, @RequestParam(required = false) String EQ_name,
			@RequestParam(required = false) String WD_name, @RequestParam(required = false) String HASATTR,
			@RequestParam(required = false) Integer maxElementCount,

			@RequestParam(required = false) String format, @RequestParam(required = false) String userID,
			@RequestParam(required = false) String accessToken,

			@RequestParam Map<String, String> params) {

		HttpHeaders responseHeaders = new HttpHeaders();
		if (format != null && format.equals("JSON")) {
			responseHeaders.add("Content-Type", "application/json; charset=utf-8");
		} else {
			responseHeaders.add("Content-Type", "application/xml; charset=utf-8");
		}
		// Access Control is not mandatory
		// However, if fid and accessToken provided, more information provided
		FacebookClient fc = null;
		List<String> friendList = null;
		if (userID != null) {
			// Check accessToken
			fc = OAuthUtil.isValidatedFacebookClient(accessToken, userID);
			if (fc == null) {
				return new ResponseEntity<>(new String("Unauthorized Token"), responseHeaders, HttpStatus.UNAUTHORIZED);
			}
			friendList = new ArrayList<String>();

			Connection<User> friendConnection = fc.fetchConnection("me/friends", User.class);
			for (List<User> friends : friendConnection) {
				for (User friend : friends) {
					friendList.add(friend.getId());
				}
			}
		}

		if (Configuration.backend.equals("MongoDB")) {
			MongoQueryService mongoQueryService = new MongoQueryService();
			String result = mongoQueryService.poll(queryName, eventType, GE_eventTime, LT_eventTime, GE_recordTime,
					LT_recordTime, EQ_action, EQ_bizStep, EQ_disposition, EQ_readPoint, WD_readPoint, EQ_bizLocation,
					WD_bizLocation, EQ_transformationID, MATCH_epc, MATCH_parentID, MATCH_inputEPC, MATCH_outputEPC,
					MATCH_anyEPC, MATCH_epcClass, MATCH_inputEPCClass, MATCH_outputEPCClass, MATCH_anyEPCClass,
					EQ_quantity, GT_quantity, GE_quantity, LT_quantity, LE_quantity, EQ_eventID,
					EXISTS_errorDeclaration, GE_errorDeclarationTime, LT_errorDeclarationTime, EQ_errorReason,
					EQ_correctiveEventID, orderBy, orderDirection, eventCountLimit, maxEventCount, vocabularyName,
					includeAttributes, includeChildren, attributeNames, EQ_name, WD_name, HASATTR, maxElementCount,
					format, userID, friendList, params);
			return new ResponseEntity<>(result, responseHeaders, HttpStatus.OK);
		} else if (Configuration.backend.equals("Cassandra")) {
			return null;
		} else if (Configuration.backend.equals("MySQL")) {
			return null;
		}

		return null;
	}
}
