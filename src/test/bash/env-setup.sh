#!/usr/bin/env bash

ZOOKEEPER=localhost
CONFLUENT_PLATFORM=5.1.2

export PATH=/opt/confluent/confluent-$CONFLUENT_PLATFORM/bin:$PATH

echo "#### Start Confluent"
confluent start

echo "#### Create streams input/output"
kafka-topics --create --zookeeper $ZOOKEEPER:2181 \
--replication-factor 1 --partitions 1 --topic streams-input \
--if-not-exists

kafka-topics --create --zookeeper $ZOOKEEPER:2181 \
--replication-factor 1 --partitions 1 --topic streams-output \
--if-not-exists

echo "#### Creating cef topic..."
kafka-topics --create --zookeeper $ZOOKEEPER:2181 \
--replication-factor 1 --partitions 1 --topic cef \
--config retention.bytes=50000000000 --config retention.ms=604800000 --config cleanup.policy=delete \
--if-not-exists

echo "#### Creating kstream-cef-avro topic..."
kafka-topics --create --zookeeper $ZOOKEEPER:2181 \
--replication-factor 1 --partitions 1 --topic kstream-cef-avro \
--config retention.bytes=50000000000 --config retention.ms=604800000 --config cleanup.policy=delete \
--if-not-exists

echo
echo "#### Loading Blue Coat cef data..."
confluent load bluecoat-cef -d $HOME/Development/data/kstream/bluecoat-cef-connector.json
