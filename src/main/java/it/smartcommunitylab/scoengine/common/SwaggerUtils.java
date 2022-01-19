package it.smartcommunitylab.scoengine.common;
//package it.smartcommunitylab.scoengine.util;
//
//import io.swagger.client.*;
//import io.swagger.client.api.BulkOperationApi;
//import io.swagger.client.auth.*;
//import io.swagger.client.model.*;
//
//public class SwaggerUtils {
//	BulkOperationApi apiInstance = new BulkOperationApi();
//	
//	private Occupations search() {
//		
//        String curie = curie_example; // String | The prefix of the provided URIs as parameters. CURIE (or Compact URI) defines a generic, abbreviated syntax for expressing Uniform Resource Identifiers (URIs).
//        array[String] uris = ; // array[String] | The array of unique identifiers of the requested resources or the array of the end of unique identifiers if CURIE is defined.
//        String language = language_example; // String | The default language of the returned response. Overwrites the Accept-Language header.
//        String selectedVersion = selectedVersion_example; // String | The selected ESCO dataset version.
//        Boolean viewObsolete = true; // Boolean | If set to 'true', the obsoleted concepts will be returned
//        String acceptLanguage = acceptLanguage_example; // String | The default language of the returned response. Optional and might be overwritten by the language request parameter.
//        try {
//            Occupations result = apiInstance.getOccupationByUri(curie, uris, language, selectedVersion, viewObsolete, acceptLanguage);
//            System.out.println(result);
//        } catch (ApiException e) {
//            System.err.println("Exception when calling BulkOperationApi#getOccupationByUri");
//            e.printStackTrace();
//        }
//	}
//	
//}
//
