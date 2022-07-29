/*
* MIT License
* 
* Copyright (c) 2022 Leonardo de Lima Oliveira
* 
* https://github.com/l3onardo-oliv3ira
* 
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/


package com.github.utils4j.imp;

import static com.github.utils4j.imp.Throwables.tryRun;
import static org.bouncycastle.jce.provider.BouncyCastleProvider.PROVIDER_NAME;

import java.security.AuthProvider;
import java.security.Provider;
import java.security.Security;

import org.apache.hc.core5.function.Supplier;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jcp.xml.dsig.internal.dom.XMLDSigRI;

import com.github.utils4j.IProviderInstaller;

@SuppressWarnings("restriction")
public enum ProviderInstaller implements IProviderInstaller {

  JSR105(){
    private final String JSR_105_PROVIDER = "XMLDSig"; //org.jcp.xml.dsig.internal.dom.XMLDSigRI
    @Override
    public Provider install(String providerName, Object config) {
      return setup(JSR_105_PROVIDER, () -> new XMLDSigRI());
    }
  },
  
  BC(){
    @Override
    public Provider install(String providerName, Object config) {
      return setup(PROVIDER_NAME, () -> {
        System.setProperty("org.bouncycastle.asn1.allow_unsafe_integer", "true");
        return new BouncyCastleProvider();
      });
    }
  },
  
  SUNPKCS11(){
    @Override
    public Provider install(String providerName, Object config) {
      Args.requireText(providerName, "provider name is empty");
      Args.requireNonNull(config, "config is null");
      final String sunProviderName = "SunPKCS11-" + providerName;
      return setup(sunProviderName, () -> SunPKCS11Creator.create(config.toString()));
    }
  };
  
  protected Provider setup(String providerName, Supplier<Provider> supplier) {
    Provider provider = Security.getProvider(providerName);
    if (provider != null)
      return provider;
    int code = Security.addProvider(provider = supplier.get());
    if (code < 0)
      throw new RuntimeException("Unabled to install provider " + providerName);
    return provider;
  }
  
  public static void uninstall(Provider provider) {
    if (provider != null) {
      if (provider instanceof AuthProvider)
        tryRun(() -> ((AuthProvider)provider).logout());
      tryRun(() -> Security.removeProvider(provider.getName()));
      tryRun(() -> provider.clear());
    }
  }
}
