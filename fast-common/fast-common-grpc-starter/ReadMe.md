spring cloud 之 grpc抽取通用protobuf锲约

# 前言

> RPC 使用 ProtoBuf 来定义服务，ProtoBuf 是由 Google 开发的一种数据序列化协议（类似于 XML、JSON、hessian）。
ProtoBuf 能够将数据进行序列化，并广泛应用在数据存储、通信协议等方面。不过，当前 gRPC 仅支持 Protobuf ，且不支持在浏览器中使用。
由于 gRPC 的设计能够支持支持多种数据格式，所以读者能够很容易实现对其他数据格式（如 XML、JSON 等）的支持。

虽然性能上给我们带来了很大的提升，但是当有很多