package com.simple.server.utils;

import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

class ResettableStreamHttpServletRequest extends HttpServletRequestWrapper {

	private byte[] rawData;
	private final HttpServletRequest request;
	private final ResettableServletInputStream servletStream;

	public ResettableStreamHttpServletRequest(HttpServletRequest request) {
		super(request);
		this.request = request;
		this.servletStream = new ResettableServletInputStream();
	}


	public void resetInputStream() {
		servletStream.stream = new ByteArrayInputStream(rawData);
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		if (rawData == null) {
			rawData = IOUtils.toByteArray(this.request.getReader());
			servletStream.stream = new ByteArrayInputStream(rawData);
		}
		return servletStream;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		if (rawData == null) {
			rawData = IOUtils.toByteArray(this.request.getReader());
			servletStream.stream = new ByteArrayInputStream(rawData);
		}
		return new BufferedReader(new InputStreamReader(servletStream));
	}


	private static class ResettableServletInputStream extends ServletInputStream {

		private InputStream stream;

		@Override
		public int read() throws IOException {
			return stream.read();
		}

		@Override
		public boolean isFinished() {
			throw new RuntimeException("Unsupported method operation: isFinished()");
		}

		@Override
		public boolean isReady() {
			throw new RuntimeException("Unsupported method operation: isReady()");
		}

		@Override
		public void setReadListener(ReadListener readListener) {
			throw new RuntimeException("Unsupported method operation: setReadListener()");
		}
	}
}