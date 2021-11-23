##学习地址
https://blog.csdn.net/mingongge/article/details/100613465

## Kubernetes与Docker Swarm的区别如何？
![](https://img-blog.csdnimg.cn/img_convert/22939f8208e0caabc4dbd7245b4c3628.png)

## 什么是Kubernetes？
![](https://img-blog.csdnimg.cn/img_convert/1110de7d0eccf8e25cf4be9033e3a604.png)

Kubernetes是一个开源容器管理工具，负责容器部署，容器扩缩容以及负载平衡。作为Google的创意之作，
它提供了出色的社区，并与所有云提供商合作。因此，我们可以说Kubernetes不是一个容器化平台，而是一个多容器管理解决方案。

## Kubernetes与Docker有什么关系？
众所周知，Docker提供容器的生命周期管理，Docker镜像构建运行时容器。但是，由于这些单独的容器必须通信，
因此使用Kubernetes。因此，我们说Docker构建容器，这些容器通过Kubernetes相互通信。
因此，可以使用Kubernetes手动关联和编排在多个主机上运行的容器。

## 在主机和容器上部署应用程序有什么区别？
![](https://img-blog.csdnimg.cn/img_convert/07c7593af4164151cb159b83174e20c4.png)

请参考上图。左侧架构表示在主机上部署应用程序。因此，这种架构将具有操作系统，然后操作系统将具有内核，
该内核将在应用程序所需的操作系统上安装各种库。因此，在这种框架中，您可以拥有n个应用程序，
并且所有应用程序将共享该操作系统中存在的库，而在容器中部署应用程序时，体系结构则略有不同。

这种架构将有一个内核，这是唯一一个在所有应用程序之间唯一共同的东西。因此，如果有一个需要Java的特定应用程序，
那么我们将获得访问Java的特定应用程序，如果有另一个需要Python的应用程序，则只有该特定应用程序才能访问Python。

您可以在图表右侧看到的各个块基本上是容器化的，并且这些块与其他应用程序隔离。
因此，应用程序具有与系统其余部分隔离的必要库和二进制文件，并且不能被任何其他应用程序侵占。

## 什么是Container Orchestration？
考虑一个应用程序有5-6个微服务的场景。现在，这些微服务被放在单独的容器中，但如果没有容器编排就无法进行通信。
因此，由于编排意味着所有乐器在音乐中和谐共处，所以类似的容器编排意味着各个容器中的所有服务协同工作以满足单个服务器的需求。

## Container Orchestration需要什么？
考虑到你有5-6个微服务用于执行各种任务的单个应用程序，所有这些微服务都放在容器中。现在，为了确保这些容器彼此通信，
我们需要容器编排。

![](https://img-blog.csdnimg.cn/img_convert/cd973ad7fdfea4b3d1e9da0c7f2e3095.png)

正如您在上图中所看到的，在没有使用容器编排的情况下，还存在许多挑战。因此，为了克服这些挑战，容器编排就到位了。

## Kubernetes有什么特点？
Kubernetes的功能如下：

![](https://img-blog.csdnimg.cn/img_convert/a9364879fb6d434fc6f8ad72669c0140.png)

## Kubernetes如何简化容器化部署？
由于典型应用程序将具有跨多个主机运行的容器集群，因此所有这些容器都需要相互通信。因此，要做到这一点，
你需要一些能够负载平衡，扩展和监控容器的东西。由于Kubernetes与云无关并且可以在任何公共/私有提供商上运行，
因此必须是您简化容器化部署的选择。

## 您对Kubernetes的集群了解多少？
Kubernetes背后的基础是我们可以实施所需的状态管理，我的意思是我们可以提供特定配置的集群服务，并且集群服务将在基础架构中运行并运行该配置。

![](https://img-blog.csdnimg.cn/img_convert/8488b544eb65492fd7f7fe70bd465fa3.png)

因此，正如您在上图中所看到的，部署文件将具有提供给集群服务所需的所有配置。现在，部署文件将被提供给API，
然后由集群服务决定如何在环境中安排这些pod，并确保正确运行的pod数量。

因此，位于服务前面的API，工作节点和节点运行的Kubelet进程，共同构成了Kubernetes集群。

## 什么是Google容器引擎？
Google Container Engine（GKE）是Docker容器和集群的开源管理平台。这个基于Kubernetes的引擎仅支持在Google的公共云服务中运行的群集。

## 什么是Heapster？
Heapster是由每个节点上运行的Kubelet提供的集群范围的数据聚合器。此容器管理工具在Kubernetes集群上本机支持，
并作为pod运行，就像集群中的任何其他pod一样。因此，它基本上发现集群中的所有节点，并通过机上Kubernetes代理查询集群中Kubernetes节点的使用信息。

## 什么是Minikube？
Minikube是一种工具，可以在本地轻松运行Kubernetes。这将在虚拟机中运行单节点Kubernetes群集。

## 什么是Kubectl？
Kubectl是一个平台，您可以使用该平台将命令传递给集群。因此，它基本上为CLI提供了针对Kubernetes集群运行命令的方法，以及创建和管理Kubernetes组件的各种方法。

## 什么是Kubelet？
这是一个代理服务，它在每个节点上运行，并使从服务器与主服务器通信。因此，Kubelet处理PodSpec中提供给它的容器的描述，并确保PodSpec中描述的容器运行正常。

## 你对Kubernetes的一个节点有什么了解？
![](https://img-blog.csdnimg.cn/img_convert/7242528c76df77c16f5151b418caf1c8.png)








