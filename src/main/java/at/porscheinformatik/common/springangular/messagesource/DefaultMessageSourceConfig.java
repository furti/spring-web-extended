package at.porscheinformatik.common.springangular.messagesource;

import java.util.HashSet;
import java.util.Set;

public class DefaultMessageSourceConfig implements MessageSourceConfig {

	private Integer cacheSeconds;
	private Set<String> baseNames;

	public DefaultMessageSourceConfig() {
		baseNames = new HashSet<>();
	}

	@Override
	public Integer getCacheSeconds() {
		return cacheSeconds;
	}

	@Override
	public Set<String> getBaseNames() {
		return baseNames;
	}

	@Override
	public void addBaseName(String baseName) {
		baseNames.add(baseName);
	}

	@Override
	public void removeBaseName(String baseName) {
		baseNames.remove(baseName);
	}

	@Override
	public void setCacheSeconds(Integer seconds) {
		this.cacheSeconds = seconds;
	}
}
