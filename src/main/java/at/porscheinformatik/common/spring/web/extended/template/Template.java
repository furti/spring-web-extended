package at.porscheinformatik.common.spring.web.extended.template;

import java.io.IOException;
import java.util.Date;

public interface Template
{

	String render() throws IOException;

	void refresh() throws IOException;

	boolean isChanged(Date since) throws IOException;
}
