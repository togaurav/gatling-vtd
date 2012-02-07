/**
 * Copyright 2011-2012 eBusiness Information, Groupe Excilys (www.excilys.com)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package com.excilys.ebi.gatling.vtd.http.check.body
import com.excilys.ebi.gatling.core.check.CheckContext.{ setAndReturnCheckContextAttribute, getCheckContextAttribute }
import com.excilys.ebi.gatling.core.session.Session
import com.excilys.ebi.gatling.http.check.body.HttpBodyCheckBuilder
import com.excilys.ebi.gatling.vtd.check.extractor.VTDXPathExtractor
import com.ning.http.client.Response

object HttpBodyVTDXPathCheckBuilder {

	def vtdXpath(expression: Session => String) = new HttpBodyCheckBuilder(findExtractorFactory, findAllExtractoryFactory, countExtractoryFactory, expression)

	private val HTTP_BODY_VTD_XPATH_EXTRACTOR_CONTEXT_KEY = "HttpBodyVTDXPathExtractor"

	private def getCachedExtractor(response: Response) = getCheckContextAttribute(HTTP_BODY_VTD_XPATH_EXTRACTOR_CONTEXT_KEY).getOrElse {
		setAndReturnCheckContextAttribute(HTTP_BODY_VTD_XPATH_EXTRACTOR_CONTEXT_KEY, new VTDXPathExtractor(response.getResponseBodyAsBytes))
	}

	private def findExtractorFactory(occurrence: Int) = (response: Response) => getCachedExtractor(response).extractOne(occurrence)(_)
	private val findAllExtractoryFactory = (response: Response) => getCachedExtractor(response).extractMultiple(_)
	private val countExtractoryFactory = (response: Response) => getCachedExtractor(response).count(_)
}