FROM docker.elastic.co/elasticsearch/elasticsearch:7.5.0

ENV VERSION=7.5.0

# https://github.com/medcl/elasticsearch-analysis-ik/releases
ADD https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v${VERSION}/elasticsearch-analysis-ik-$VERSION.zip /tmp/
RUN /usr/share/elasticsearch/bin/elasticsearch-plugin install -b file:///tmp/elasticsearch-analysis-ik-$VERSION.zip

RUN rm -rf /tmp/*