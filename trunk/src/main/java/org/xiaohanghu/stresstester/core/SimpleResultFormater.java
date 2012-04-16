package org.xiaohanghu.stresstester.core;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author xiaohanghu
 * */
public class SimpleResultFormater implements StressResultFormater {
	public static final Log log = LogFactory.getLog(SimpleResultFormater.class);

	public void format(StressResult stressResult, Writer writer) {
		long testsTakenTime = stressResult.getTestsTakenTime();
		int totalRequests = stressResult.getTotalRequests();
		int concurrencyLevel = stressResult.getConcurrencyLevel();

		float takes = StatisticsUtils.toMs(testsTakenTime);

		List<Long> allTimes = stressResult.getAllTimes();
		long totaleTimes = StatisticsUtils.getTotal(allTimes);

		// float tps = (totalRequests * 1000) / takes;
		float tps = 1000 * 1000000 * (concurrencyLevel * (totalRequests / (float) totaleTimes));

		float averageTime = StatisticsUtils.getAverage(totaleTimes,
				totalRequests);
		/** 理论单线程请求响应时间 */
		float onTheadAverageTime = averageTime / concurrencyLevel;

		int count_50 = totalRequests / 2;
		int count_66 = totalRequests * 66 / 100;
		int count_75 = totalRequests * 75 / 100;
		int count_80 = totalRequests * 80 / 100;
		int count_90 = totalRequests * 90 / 100;
		int count_95 = totalRequests * 95 / 100;
		int count_98 = totalRequests * 98 / 100;
		int count_99 = totalRequests * 99 / 100;

		long longestRequest = allTimes.get(allTimes.size() - 1);
		long shortestRequest = allTimes.get(0);

		StringBuilder view = new StringBuilder();

		// if (StringUtils.isNotBlank(serviceName)) {
		// view.append(" Service Name:\t").append(serviceName);
		// view.append("\r\n");
		// }
		view.append(" Concurrency Level:\t").append(concurrencyLevel);
		view.append("\r\n Time taken for tests:\t").append(takes).append(" ms");
		view.append("\r\n Complete Requests:\t").append(totalRequests);
		view.append("\r\n Failed Requests:\t").append(
				stressResult.getFailedRequests());
		view.append("\r\n Requests per second:\t").append(tps);
		view.append("\r\n Time per request:\t")
				.append(StatisticsUtils.toMs(averageTime)).append(" ms");
		view.append("\r\n Time per request:\t")
				.append(StatisticsUtils.toMs(onTheadAverageTime))
				.append(" ms (across all concurrent requests)");
		view.append("\r\n Shortest request:\t")
				.append(StatisticsUtils.toMs(shortestRequest)).append(" ms");

		StringBuilder certainTimeView = view;
		certainTimeView
				.append("\r\n Percentage of the requests served within a certain time (ms)");
		certainTimeView.append("\r\n  50%\t").append(
				StatisticsUtils.toMs(allTimes.get(count_50)));
		certainTimeView.append("\r\n  66%\t").append(
				StatisticsUtils.toMs(allTimes.get(count_66)));
		certainTimeView.append("\r\n  75%\t").append(
				StatisticsUtils.toMs(allTimes.get(count_75)));
		certainTimeView.append("\r\n  80%\t").append(
				StatisticsUtils.toMs(allTimes.get(count_80)));
		certainTimeView.append("\r\n  90%\t").append(
				StatisticsUtils.toMs(allTimes.get(count_90)));
		certainTimeView.append("\r\n  95%\t").append(
				StatisticsUtils.toMs(allTimes.get(count_95)));
		certainTimeView.append("\r\n  98%\t").append(
				StatisticsUtils.toMs(allTimes.get(count_98)));
		certainTimeView.append("\r\n  99%\t").append(
				StatisticsUtils.toMs(allTimes.get(count_99)));
		certainTimeView.append("\r\n 100%\t")
				.append(StatisticsUtils.toMs(longestRequest))
				.append(" (longest request)");

		try {
			writer.write(view.toString());
		} catch (IOException e) {
			log.error("IOException:", e);
		}

	}


}
