/**
 * Copyright 2014 Daniel Furtlehner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.porscheinformatik.common.spring.web.extended.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.parboiled.errors.ParseError;
import org.parboiled.support.ParsingResult;
import org.springframework.util.Assert;

public class ParboiledUtils
{

	public static <T> List<T> buildFromResult(ParsingResult<T> result,
			String resourceDescription)
	{
		Assert.notNull(result, "Got null Result while parsing template "
				+ resourceDescription);

		if (result.hasErrors())
		{
			throw new IllegalArgumentException(buildErrorMessag(result,
					resourceDescription));
		}

		Assert.isTrue(result.matched,
				"Template " + resourceDescription
						+ " does not match the required format ");

		List<T> parts = new ArrayList<>(result.valueStack.size());

		for (T part : result.valueStack)
		{
			parts.add(0, part);
		}

		return parts;
	}

	private static <T> String buildErrorMessag(ParsingResult<T> result,
			String resourceDescription)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Template " + resourceDescription
				+ " contains errors: \n");
		for (ParseError error : result.parseErrors)
		{
			int start = error.getStartIndex();
			int end = error.getEndIndex();

			if (start - 10 > 0)
			{
				start -= 10;
			}
			else
			{
				start = 0;
			}

			builder.append(
					error.getInputBuffer().extract(start
							, end))
					.append(" --> ");

			if (error.getErrorMessage() != null)
			{
				builder.append(error.getErrorMessage());
			}
			else
			{
				builder.append(error.toString());
			}

			builder.append("\n");

			builder.append(StringUtils.leftPad("^",
					error.getStartIndex() - start - 1));

			if (error.getEndIndex() > error.getStartIndex())
			{
				builder.append(StringUtils.left("^", error.getEndIndex()
						- error.getStartIndex() - 1));
			}
		}

		return builder.toString();
	}
}
