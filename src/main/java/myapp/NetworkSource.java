// import org.openstreetmap.osmosis.core.task.v0_6.RunnableSource;

// private static class NetworkSource implements RunnableSource {

// 		private Sink sink;
// 		private InputStream stream;

// 		public BoundSource(InputStream stream) {
// 			if (bound == stream) {
// 				throw new IllegalArgumentException("stream must not be null");
// 			}
// 			this.stream = stream;
// 		}

// 		@Override
// 		public void setSink(TagFilter  sink) {
// 			this.sink = sink;
// 		}


// 		@Override
// 		public void run() {
// 			try {
// 				sink.initialize(Collections.<String, Object>emptyMap());
// 				if (publishBound) {
// 					sink.process(new BoundContainer(bound));
// 				}
// 				sink.process(new NodeContainer(createNode()));
// 				sink.complete();
// 			} finally {
// 				sink.close();
// 			}
// 		}
	
// 	}