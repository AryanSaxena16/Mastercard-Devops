# Networking Fundamentals — A to Z Beginner Guide

> **Goal:** Understand networking from the ground up, especially the concepts you need for Linux, DevOps, system administration, and backend work.
>
> **Level:** Beginner → Intermediate  
> **Style:** Simple language, examples, commands, and practical understanding.

---

## Table of Contents

1. [What Is a Network?](#1-what-is-a-network)
2. [Important Networking Terms](#2-important-networking-terms)
3. [IP Addressing](#3-ip-addressing)
4. [IPv4](#4-ipv4)
5. [IPv4 Address Classes](#5-ipv4-address-classes)
6. [Private and Public IP Addresses](#6-private-and-public-ip-addresses)
7. [Subnet Mask](#7-subnet-mask)
8. [CIDR](#8-cidr)
9. [Network Address, Host Address, Broadcast Address](#9-network-address-host-address-broadcast-address)
10. [Default Gateway](#10-default-gateway)
11. [MAC Address](#11-mac-address)
12. [ARP](#12-arp)
13. [IPv6](#13-ipv6)
14. [IPv4 vs IPv6](#14-ipv4-vs-ipv6)
15. [DNS](#15-dns)
16. [DNS Resolution](#16-dns-resolution)
17. [DNS Record Types](#17-dns-record-types)
18. [DHCP](#18-dhcp)
19. [Ports](#19-ports)
20. [Default / Common Ports](#20-default--common-ports)
21. [TCP](#21-tcp)
22. [UDP](#22-udp)
23. [TCP vs UDP](#23-tcp-vs-udp)
24. [HTTP](#24-http)
25. [HTTPS](#25-https)
26. [HTTP vs HTTPS](#26-http-vs-https)
27. [OSI Model](#27-osi-model)
28. [TCP/IP Model](#28-tcpip-model)
29. [How a Website Request Works](#29-how-a-website-request-works)
30. [Linux Networking Commands](#30-linux-networking-commands)
31. [Practical Linux Networking Examples](#31-practical-linux-networking-examples)
32. [Troubleshooting Method](#32-troubleshooting-method)
33. [Useful Networking Files in Linux](#33-useful-networking-files-in-linux)
34. [Firewall Basics](#34-firewall-basics)
35. [Networking Terms You Should Know](#35-networking-terms-you-should-know)
36. [Beginner Practice Scenarios](#36-beginner-practice-scenarios)
37. [Quick Revision](#37-quick-revision)

---

# 1. What Is a Network?

A **network** is a group of devices that can communicate with each other.

Examples:

- Your laptop communicating with your Wi-Fi router.
- Your phone communicating with a printer.
- A Linux server communicating with a database server.
- Your browser communicating with Google's servers.

A network allows devices to:

- Send data.
- Receive data.
- Share resources.
- Access applications.
- Access the internet.

### Simple example

Imagine an office:

```text
Laptop A ----\
Laptop B ----- Router ----- Internet
Laptop C ----/
```

The router helps devices communicate with other networks.

---

# 2. Important Networking Terms

Before learning IP addressing, understand these words.

## Host

A **host** is a device connected to a network.

Examples:

- Laptop
- Server
- Phone
- VM
- Container
- Printer

## Client

A **client** requests a service.

Example:

```text
Browser → Web Server
```

The browser is the client.

## Server

A **server** provides a service.

Example:

```text
Browser → Web Server
```

The web server provides the website.

## Router

A router connects different networks.

Example:

```text
Home Network → Router → Internet
```

## Switch

A switch connects devices within the same local network.

Example:

```text
PC A ----\
PC B ----- Switch
PC C ----/
```

## Packet

Data sent across a network is broken into smaller pieces called **packets**.

Think of a packet like a small parcel containing:

- Source information
- Destination information
- Actual data
- Control information

---

# 3. IP Addressing

An **IP address** identifies a device/interface on an IP network.

Example IPv4 address:

```text
192.168.1.10
```

You can think of it like a postal address for network communication.

If computer A wants to communicate with computer B:

```text
Computer A
192.168.1.10
      |
      | packet
      v
Computer B
192.168.1.20
```

The IP address helps determine where the packet should go.

## Why do we need IP addresses?

Suppose 100 computers are connected.

How would one computer know which computer should receive a packet?

IP addressing provides logical addressing so devices can communicate across networks.

---

# 4. IPv4

**IPv4** stands for **Internet Protocol version 4**.

An IPv4 address contains **32 bits**.

It is normally written as four decimal numbers separated by dots.

Example:

```text
192.168.1.10
```

Each number is called an **octet**.

```text
192 . 168 . 1 . 10
 |     |    |    |
octet octet octet octet
```

Each octet can contain:

```text
0 to 255
```

Therefore:

```text
0.0.0.0
```

through

```text
255.255.255.255
```

are the possible IPv4 address values.

## Why 0–255?

Each octet contains 8 bits.

8 bits can represent:

```text
2^8 = 256
```

values.

Those values are:

```text
0 through 255
```

## Binary representation

Example:

```text
192.168.1.10
```

is represented internally using binary.

You do not normally need to convert every IP manually, but understanding that IPv4 is 32 bits is important.

---

# 5. IPv4 Address Classes

You may hear about old IPv4 classes:

| Class | Range | Traditional purpose |
|---|---|---|
| A | 1–126 | Very large networks |
| B | 128–191 | Medium networks |
| C | 192–223 | Smaller networks |
| D | 224–239 | Multicast |
| E | 240–255 | Experimental |

### Important

Modern networks generally use **CIDR**, not the old class-based system.

So learn address classes for understanding historical terminology, but focus more on:

- Subnet masks
- CIDR
- Network addresses
- Host addresses

---

# 6. Private and Public IP Addresses

## Private IP

Private IP addresses are used inside private networks.

Common private IPv4 ranges are:

```text
10.0.0.0/8
172.16.0.0/12
192.168.0.0/16
```

Examples:

```text
10.0.0.5
172.16.10.20
192.168.1.10
```

These addresses are normally not directly routable across the public internet.

## Public IP

A public IP address is used for communication across the public internet.

Example:

```text
203.0.113.10
```

> `203.0.113.0/24` is reserved for documentation/examples, so do not treat that example as a real public server.

## Simple picture

```text
Private Network

Laptop
192.168.1.10
     |
     v
Router
Public IP
     |
     v
Internet
```

The router commonly uses **NAT** to translate private addresses to a public address.

---

# 7. Subnet Mask

A subnet mask tells us which part of an IPv4 address represents the **network** and which part represents the **host**.

Example:

```text
IP:
192.168.1.10

Subnet mask:
255.255.255.0
```

The same mask can be written as:

```text
/24
```

So:

```text
192.168.1.10/24
```

means:

- First 24 bits = network portion.
- Remaining 8 bits = host portion.

## Common subnet masks

| CIDR | Subnet mask |
|---|---|
| /8 | 255.0.0.0 |
| /16 | 255.255.0.0 |
| /24 | 255.255.255.0 |
| /25 | 255.255.255.128 |
| /26 | 255.255.255.192 |
| /27 | 255.255.255.224 |
| /28 | 255.255.255.240 |
| /30 | 255.255.255.252 |

---

# 8. CIDR

**CIDR** stands for **Classless Inter-Domain Routing**.

Instead of writing:

```text
192.168.1.10
255.255.255.0
```

we can write:

```text
192.168.1.10/24
```

The `/24` tells us that the first 24 bits are the network portion.

## Why CIDR is useful

CIDR allows networks to be divided into different sizes.

For example:

```text
192.168.1.0/24
```

is larger than:

```text
192.168.1.0/28
```

A `/28` provides 16 total IPv4 addresses.

Traditionally, 14 are usable for hosts because:

- 1 is the network address.
- 1 is the broadcast address.

---

# 9. Network Address, Host Address, Broadcast Address

Consider:

```text
192.168.1.10/24
```

The network is:

```text
192.168.1.0/24
```

The traditional IPv4 broadcast address is:

```text
192.168.1.255
```

Host addresses are typically:

```text
192.168.1.1
192.168.1.2
...
192.168.1.254
```

## Three important concepts

### Network address

Identifies the network itself.

```text
192.168.1.0
```

### Host address

Identifies a device/interface inside the network.

```text
192.168.1.10
```

### Broadcast address

Used in IPv4 to send traffic to all hosts on the local subnet.

```text
192.168.1.255
```

> IPv6 does not use broadcast in the same way; it uses multicast instead.

---

# 10. Default Gateway

The **default gateway** is normally the router that a device uses to reach other networks.

Example:

```text
Laptop
192.168.1.10
     |
     v
Gateway
192.168.1.1
     |
     v
Internet
```

If your laptop wants to communicate with:

```text
8.8.8.8
```

it knows that `8.8.8.8` is outside its local network.

So it sends the packet to its default gateway.

---

# 11. MAC Address

A **MAC address** is a link-layer address associated with a network interface.

Example:

```text
00:1A:2B:3C:4D:5E
```

MAC addresses are commonly 48 bits and written in hexadecimal.

## IP vs MAC

Think of them as different types of addresses:

```text
IP address
= logical network address

MAC address
= local network interface address
```

IP addresses are used for routing between networks.

MAC addresses are used for local network delivery.

---

# 12. ARP

**ARP** means **Address Resolution Protocol**.

IPv4 devices use ARP to discover the MAC address associated with an IPv4 address on the local network.

Example:

Computer A knows:

```text
192.168.1.20
```

but needs the destination MAC address.

It can ask:

```text
Who has 192.168.1.20?
```

The device with that IP responds with its MAC address.

Then communication can happen on the local Ethernet network.

## Linux command

```bash
ip neigh
```

This shows the neighbor/ARP-related cache.

---

# 13. IPv6

**IPv6** stands for **Internet Protocol version 6**.

IPv6 uses **128-bit addresses**.

Example:

```text
2001:db8:abcd:0012::1
```

IPv6 addresses are written using hexadecimal and colons.

## Why IPv6?

IPv4 has a limited address space.

IPv6 provides a vastly larger address space.

IPv6 also includes features designed for modern networking, including:

- Huge address space
- Stateless address configuration
- Multicast
- No IPv4-style broadcast
- Simplified base header

## IPv6 shorthand

This:

```text
2001:0db8:0000:0000:0000:0000:0000:0001
```

can be shortened to:

```text
2001:db8::1
```

Rules include:

1. Leading zeros in each group can be removed.
2. One consecutive run of zero groups can be replaced by `::`.

You can use `::` only once in an address.

---

# 14. IPv4 vs IPv6

| Feature | IPv4 | IPv6 |
|---|---|---|
| Address size | 32-bit | 128-bit |
| Example | 192.168.1.10 | 2001:db8::1 |
| Format | Decimal + dots | Hexadecimal + colons |
| Broadcast | Yes | No |
| Address availability | Limited | Extremely large |
| ARP | Used | Neighbor Discovery instead |
| NAT | Common | Not fundamentally required for address shortage |

### Simple way to remember

```text
IPv4 → 32-bit → 192.168.1.10

IPv6 → 128-bit → 2001:db8::1
```

---

# 15. DNS

**DNS** stands for **Domain Name System**.

DNS translates human-friendly domain names into IP addresses.

For example:

```text
google.com
     |
     v
DNS
     |
     v
IP address
```

Humans prefer:

```text
example.com
```

Computers communicate using IP addresses.

DNS connects the two.

## Why DNS is needed

Imagine having to remember the IP address of every website.

Instead of:

```text
142.x.x.x
```

you can type:

```text
example.com
```

---

# 16. DNS Resolution

**DNS resolution** is the process of finding the IP address associated with a domain name.

Suppose you enter:

```text
www.example.com
```

A simplified process is:

```text
Browser
   |
   v
Operating System DNS cache
   |
   v
Configured DNS resolver
   |
   v
Root DNS servers
   |
   v
TLD server (.com)
   |
   v
Authoritative DNS server
   |
   v
IP address
```

In real life, caching can make the process much shorter.

## Recursive DNS resolver

A DNS resolver performs DNS lookups on behalf of clients.

Your computer might be configured to use a resolver provided by:

- Your router
- Your ISP
- Your organization
- A public DNS service

## Root server

Root DNS servers direct resolvers toward the appropriate TLD servers.

## TLD server

TLD means **Top-Level Domain**.

Examples:

```text
.com
.org
.net
.in
```

## Authoritative DNS server

The authoritative server contains the actual DNS records for a domain.

---

# 17. DNS Record Types

DNS can store different types of information.

## A record

Maps a hostname to an IPv4 address.

```text
example.com → 192.0.2.10
```

## AAAA record

Maps a hostname to an IPv6 address.

```text
example.com → 2001:db8::10
```

## CNAME

Creates an alias for another hostname.

Example:

```text
www.example.com
        |
        v
example.com
```

## MX

Specifies mail servers for a domain.

Used for email delivery.

## NS

Specifies authoritative name servers.

## TXT

Stores text information.

It is commonly used for:

- Domain verification
- SPF
- DKIM-related configuration
- Other domain policies

---

# 18. DHCP

**DHCP** stands for **Dynamic Host Configuration Protocol**.

DHCP automatically provides network configuration to clients.

It can provide:

- IP address
- Subnet mask/prefix
- Default gateway
- DNS server information

## Simplified DHCP process

Remember:

```text
DORA
```

### D — Discover

Client asks:

```text
Is there a DHCP server?
```

### O — Offer

DHCP server offers configuration.

### R — Request

Client requests the offered configuration.

### A — Acknowledge

Server confirms the lease.

---

# 19. Ports

An IP address identifies a host/interface.

A **port** identifies a service/application endpoint on that host.

Think:

```text
IP address = building address
Port = apartment/room number
```

Example:

```text
192.168.1.10:22
```

means:

```text
IP:   192.168.1.10
Port: 22
```

Port numbers range from:

```text
0–65535
```

## Port ranges

### 0–1023

Well-known ports.

### 1024–49151

Registered ports.

### 49152–65535

Dynamic/private ports.

The exact ephemeral-port range can vary by operating system configuration.

---

# 20. Default / Common Ports

These are important for Linux and DevOps.

| Service | Protocol | Common port |
|---|---|---:|
| FTP | TCP | 21 |
| SSH | TCP | 22 |
| Telnet | TCP | 23 |
| SMTP | TCP | 25 |
| DNS | TCP/UDP | 53 |
| DHCP server | UDP | 67 |
| DHCP client | UDP | 68 |
| HTTP | TCP | 80 |
| POP3 | TCP | 110 |
| IMAP | TCP | 143 |
| HTTPS | TCP | 443 |
| SMB | TCP | 445 |
| NTP | UDP | 123 |
| LDAP | TCP/UDP | 389 |
| LDAPS | TCP | 636 |
| PostgreSQL | TCP | 5432 |
| MySQL | TCP | 3306 |
| Redis | TCP | 6379 |
| MongoDB | TCP | 27017 |

> A port number does not force a protocol or application. It is a conventional default. Administrators can configure services to listen on different ports.

---

# 21. TCP

**TCP** stands for **Transmission Control Protocol**.

TCP is connection-oriented and designed for reliable, ordered delivery.

Examples where TCP is commonly used:

- HTTPS
- SSH
- HTTP/1.1
- Many database connections

## TCP features

TCP provides:

- Connection establishment
- Reliable delivery
- Ordering
- Retransmission
- Flow control
- Congestion control

## TCP three-way handshake

Before normal TCP data transfer, a connection is established.

Simplified:

```text
Client                 Server

  SYN -------------------->

      <---------------- SYN-ACK

  ACK -------------------->
```

Now the TCP connection is established.

## Why retransmission?

Imagine sending:

```text
Packet 1
Packet 2
Packet 3
Packet 4
```

If packet 3 is lost, TCP can detect the missing data and retransmit it.

---

# 22. UDP

**UDP** stands for **User Datagram Protocol**.

UDP is connectionless and has less protocol overhead than TCP.

It does not provide TCP-style guarantees that every packet arrives or arrives in order.

Common examples:

- DNS queries
- DHCP
- Streaming/real-time applications
- Online games
- Voice/video applications

UDP is useful when speed and low overhead are more important than retransmitting every missing packet.

---

# 23. TCP vs UDP

genui{"data_networks_databases_learning_block":{"type_id":"TCP_VS_UDP","locale_override":"en-US"}}

| Feature | TCP | UDP |
|---|---|---|
| Connection | Connection-oriented | Connectionless |
| Reliability | Yes | No built-in TCP-style reliability |
| Ordering | Yes | No |
| Retransmission | Yes | No |
| Handshake | Yes | No TCP handshake |
| Overhead | Higher | Lower |
| Typical use | HTTPS, SSH, databases | DNS, DHCP, real-time traffic |

### Easy example

TCP is like a courier service that:

```text
Sends package
↓
Confirms delivery
↓
Resends if necessary
```

UDP is more like:

```text
Send the message
↓
Continue
```

If a packet is lost, UDP itself does not provide TCP-style retransmission.

---

# 24. HTTP

**HTTP** stands for **Hypertext Transfer Protocol**.

It is an application-layer protocol commonly used for web communication.

Example:

```text
Browser → HTTP request → Web Server
Browser ← HTTP response ← Web Server
```

## HTTP request

A request may contain:

```text
Method
URL/path
Headers
Body
```

Example:

```http
GET /users HTTP/1.1
Host: example.com
```

## Common HTTP methods

### GET

Retrieve data.

```http
GET /users
```

### POST

Create/send data.

```http
POST /users
```

### PUT

Replace/update a resource.

### PATCH

Partially update a resource.

### DELETE

Delete a resource.

---

# 25. HTTPS

**HTTPS** means **HTTP Secure**.

HTTPS is HTTP carried over a secure TLS connection.

Simplified:

```text
HTTP
  +
TLS
  =
HTTPS
```

HTTPS provides:

- Encryption
- Integrity protection
- Server authentication through certificates

Default port:

```text
443
```

---

# 26. HTTP vs HTTPS

| Feature | HTTP | HTTPS |
|---|---|---|
| Default port | 80 | 443 |
| Encryption | No TLS encryption | TLS encryption |
| Certificate | Not required | Normally used |
| Security | Lower | Higher |
| Common use | Unencrypted web traffic | Modern websites/APIs |

## Important

HTTPS does not mean that the website itself is trustworthy.

HTTPS protects the connection between your client and the server, but you still need to verify that you are visiting the correct website.

---

# 27. OSI Model

The **OSI model** is a conceptual model used to understand networking.

It has **7 layers**.

Remember from bottom to top:

```text
7. Application
6. Presentation
5. Session
4. Transport
3. Network
2. Data Link
1. Physical
```

A common mnemonic:

```text
All People Seem To Need Data Processing
```

---

## Layer 1 — Physical

Deals with physical transmission.

Examples:

- Cables
- Fiber
- Radio signals
- Electrical signals
- Connectors

Think:

> "How do the bits physically travel?"

---

## Layer 2 — Data Link

Deals with communication on the local network.

Examples:

- Ethernet
- Wi-Fi
- MAC addresses
- Ethernet frames

Think:

> "How do devices communicate on this local network?"

---

## Layer 3 — Network

Deals with logical addressing and routing.

Examples:

- IPv4
- IPv6
- Routers
- IP addresses

Think:

> "Which network should this packet go to?"

---

## Layer 4 — Transport

Deals with end-to-end transport.

Examples:

- TCP
- UDP
- Ports

Think:

> "Which application/service should receive this data, and how should transport happen?"

---

## Layer 5 — Session

Deals with sessions between applications.

For beginner-level Linux/DevOps work, you usually do not need to memorize detailed implementation here.

Think:

> "How is a communication session managed?"

---

## Layer 6 — Presentation

Deals with how data is represented.

Examples/concepts:

- Encoding
- Serialization
- Encryption concepts
- Compression

Think:

> "How should the data be represented?"

---

## Layer 7 — Application

Closest to the applications users interact with.

Examples:

- HTTP
- DNS
- SMTP
- SSH
- FTP

Think:

> "What network service does the application need?"

---

# 28. TCP/IP Model

In practical networking, you will also hear about the **TCP/IP model**.

A simplified version is:

```text
Application
Transport
Internet
Link
```

Mapping to OSI:

| TCP/IP | Approximate OSI layers |
|---|---|
| Application | 5, 6, 7 |
| Transport | 4 |
| Internet | 3 |
| Link | 1, 2 |

You do not need to treat these models as two completely separate networking systems.

They are models used to organize networking concepts.

---

# 29. How a Website Request Works

Let's understand the complete process.

Suppose you enter:

```text
https://example.com
```

## Step 1 — DNS

Your computer needs the server's IP address.

It performs DNS resolution:

```text
example.com
     ↓
DNS
     ↓
IP address
```

## Step 2 — Routing

Your machine determines where to send the packet.

If the destination is outside the local subnet:

```text
Computer → Default Gateway → Other Networks
```

## Step 3 — TCP

For traditional HTTPS over TCP, a TCP connection is established.

```text
Client → SYN
Server → SYN-ACK
Client → ACK
```

## Step 4 — TLS

For HTTPS, TLS negotiation/authentication happens.

The connection becomes encrypted.

## Step 5 — HTTP

The browser sends an HTTP request.

Example:

```http
GET / HTTP/1.1
Host: example.com
```

## Step 6 — Server response

The server responds.

Example:

```http
HTTP/1.1 200 OK
```

Then the response body contains the requested content.

### Overall picture

```text
Browser
   |
   | DNS
   v
IP address
   |
   | Routing
   v
Server
   |
   | TCP
   v
TLS
   |
   | HTTPS
   v
HTTP response
```

---

# 30. Linux Networking Commands

These commands are extremely useful for Linux administration and DevOps.

---

## 30.1 `ip`

The modern Linux networking command.

### Show interfaces and addresses

```bash
ip addr
```

or:

```bash
ip a
```

You may see:

```text
lo
eth0
ens33
```

along with IP addresses.

### Show interfaces

```bash
ip link
```

### Show routing table

```bash
ip route
```

Example:

```text
default via 192.168.1.1 dev eth0
192.168.1.0/24 dev eth0
```

This tells you that the default gateway is:

```text
192.168.1.1
```

---

# 30.2 `ping`

Tests basic IP reachability using ICMP echo messages.

```bash
ping 8.8.8.8
```

You can also test a hostname:

```bash
ping example.com
```

This can help determine:

- Whether a host is reachable.
- Whether DNS resolution works.
- Approximate round-trip time.

### Stop ping

Press:

```text
Ctrl+C
```

---

# 30.3 `ss`

Shows sockets and listening network services.

```bash
ss -tuln
```

Common options:

```text
-t = TCP
-u = UDP
-l = listening
-n = don't resolve names
```

Example:

```bash
ss -tuln
```

can help answer:

> Which TCP/UDP ports are currently listening on this machine?

---

# 30.4 `curl`

`curl` is extremely useful for testing HTTP/HTTPS services.

```bash
curl https://example.com
```

Show response headers:

```bash
curl -I https://example.com
```

Verbose output:

```bash
curl -v https://example.com
```

This can show details about:

- DNS
- Connection
- TLS
- HTTP request
- HTTP response

---

# 30.5 `wget`

Used to download resources.

Example:

```bash
wget https://example.com/file.txt
```

---

# 30.6 `dig`

Used for DNS queries.

Example:

```bash
dig example.com
```

Ask specifically for an A record:

```bash
dig A example.com
```

Ask for an AAAA record:

```bash
dig AAAA example.com
```

Ask for MX records:

```bash
dig MX example.com
```

---

# 30.7 `nslookup`

Another DNS lookup tool.

```bash
nslookup example.com
```

It is useful for basic DNS troubleshooting.

---

# 30.8 `traceroute`

Shows the path packets take toward a destination.

```bash
traceroute example.com
```

On some Linux systems you may need to install it first.

A modern alternative is:

```bash
tracepath example.com
```

---

# 30.9 `hostname`

Shows or manages the system hostname.

```bash
hostname
```

Example output:

```text
server01
```

---

# 30.10 `hostnamectl`

On systemd-based Linux systems:

```bash
hostnamectl
```

This gives information about the hostname and operating system.

---

# 30.11 `ip neigh`

Shows neighboring devices and their link-layer information.

```bash
ip neigh
```

Useful when investigating local-network address resolution.

---

# 30.12 `ethtool`

Displays information about network interfaces.

Example:

```bash
ethtool eth0
```

It can show:

- Link status
- Speed
- Duplex
- Supported features

You may need administrator privileges for some operations.

---

# 30.13 `nmcli`

On systems using NetworkManager:

```bash
nmcli
```

Show devices:

```bash
nmcli device status
```

Show connections:

```bash
nmcli connection show
```

---

# 30.14 `route`

Older command:

```bash
route -n
```

Modern Linux systems generally prefer:

```bash
ip route
```

---

# 30.15 `ifconfig`

Older networking command:

```bash
ifconfig
```

Modern Linux systems generally prefer:

```bash
ip addr
```

You may still encounter `ifconfig` on older systems.

---

# 31. Practical Linux Networking Examples

## Scenario 1 — "What IP address does my machine have?"

Use:

```bash
ip addr
```

Look for an interface such as:

```text
eth0
ens33
enp0s3
```

and its `inet` address.

---

## Scenario 2 — "What is my default gateway?"

Use:

```bash
ip route
```

Look for:

```text
default via ...
```

Example:

```text
default via 192.168.1.1 dev eth0
```

Gateway:

```text
192.168.1.1
```

---

## Scenario 3 — "Is another server reachable?"

Use:

```bash
ping <IP>
```

Example:

```bash
ping 192.168.1.20
```

Remember that a failed ping does **not always** mean the server is down. Firewalls may block ICMP.

---

## Scenario 4 — "What ports are listening?"

Use:

```bash
ss -tuln
```

---

## Scenario 5 — "Is my web server responding?"

Use:

```bash
curl -I http://localhost
```

or:

```bash
curl -I https://example.com
```

---

## Scenario 6 — "Is DNS working?"

Try:

```bash
dig example.com
```

or:

```bash
nslookup example.com
```

---

## Scenario 7 — "What path does traffic take?"

Use:

```bash
traceroute example.com
```

or:

```bash
tracepath example.com
```

---

## Scenario 8 — "Which DNS server am I using?"

Depending on the Linux distribution and DNS setup, inspect:

```bash
resolvectl status
```

or:

```bash
cat /etc/resolv.conf
```

Note that `/etc/resolv.conf` may point to a local resolver stub rather than showing the actual upstream DNS server.

---

# 32. Troubleshooting Method

When a service is not working, don't randomly run commands.

Follow a logical order.

## Step 1 — Is the interface up?

```bash
ip link
```

Look for:

```text
UP
```

## Step 2 — Does the machine have an IP?

```bash
ip addr
```

## Step 3 — Is there a route?

```bash
ip route
```

## Step 4 — Can I reach the gateway?

```bash
ping <gateway-ip>
```

## Step 5 — Can I reach the destination IP?

```bash
ping <destination-ip>
```

Remember: ICMP may be blocked.

## Step 6 — Does DNS work?

```bash
dig example.com
```

## Step 7 — Is the service listening?

```bash
ss -tuln
```

## Step 8 — Can I connect to the service?

For HTTP:

```bash
curl -v http://server-ip:port
```

## Step 9 — Check firewall

For example:

```bash
sudo ufw status
```

or on systems using firewalld:

```bash
sudo firewall-cmd --list-all
```

## Step 10 — Check application logs

If networking appears correct but the application still fails, investigate the application's logs.

---

# 33. Useful Networking Files in Linux

## `/etc/hosts`

Provides local hostname-to-IP mappings.

Example:

```text
127.0.0.1 localhost
192.168.1.20 server01
```

You can test:

```bash
getent hosts server01
```

---

## `/etc/resolv.conf`

Contains resolver configuration information.

Example:

```text
nameserver 192.168.1.1
```

Modern Linux systems may manage this file automatically.

---

## `/etc/hostname`

Contains the system hostname on many Linux distributions.

Check it:

```bash
cat /etc/hostname
```

---

# 34. Firewall Basics

A firewall controls network traffic according to rules.

Think:

```text
Incoming traffic
       |
       v
   Firewall
    /    \
 Allow   Block
```

Example:

A server may allow:

```text
TCP 22  → SSH
TCP 80  → HTTP
TCP 443 → HTTPS
```

while blocking other incoming ports.

## UFW

On Ubuntu systems, you may encounter:

```bash
sudo ufw status
```

Allow SSH:

```bash
sudo ufw allow 22/tcp
```

Allow HTTPS:

```bash
sudo ufw allow 443/tcp
```

## firewalld

On systems using firewalld:

```bash
sudo firewall-cmd --state
```

List rules:

```bash
sudo firewall-cmd --list-all
```

---

# 35. Networking Terms You Should Know

## Bandwidth

Maximum amount of data that can be transferred over a connection in a given time.

Example:

```text
100 Mbps
```

## Latency

Time taken for data to travel between endpoints.

Example:

```text
20 ms
```

Lower latency usually means faster response time.

## Throughput

The actual amount of data successfully transferred.

Bandwidth is the capacity; throughput is what you actually achieve.

## Packet loss

Packets fail to reach their destination.

Example:

```text
100 packets sent
5 packets lost
```

Packet loss can cause:

- Slow applications
- Broken connections
- Poor voice/video quality

## Jitter

Variation in packet arrival time.

Especially important for:

- Voice
- Video
- Real-time applications

## MTU

**Maximum Transmission Unit**.

It is the largest packet payload size that can normally be transmitted over a link without fragmentation at that layer.

A common Ethernet MTU is:

```text
1500 bytes
```

Check interface information with:

```bash
ip link
```

---

# 36. Beginner Practice Scenarios

The best way to learn networking is to solve problems.

## Scenario 1 — Find your IP

You are logged into a Linux server.

**Question:** What command shows the IP addresses assigned to the machine?

<details>
<summary>Answer</summary>

```bash
ip addr
```

</details>

---

## Scenario 2 — Find the gateway

You need to know which router your server uses to reach other networks.

**Question:** What command should you use?

<details>
<summary>Answer</summary>

```bash
ip route
```

Look for:

```text
default via ...
```

</details>

---

## Scenario 3 — Check listening ports

A developer says the application should be running on port 8080.

**Question:** How do you check whether something is listening?

<details>
<summary>Answer</summary>

```bash
ss -tuln
```

</details>

You can also filter:

```bash
ss -tuln | grep 8080
```

---

## Scenario 4 — Test a web API

An API is supposedly running on:

```text
localhost:8080
```

**Question:** What command can test it?

<details>
<summary>Answer</summary>

```bash
curl http://localhost:8080
```

</details>

---

## Scenario 5 — Test DNS

The website name isn't resolving.

**Question:** What command can you use?

<details>
<summary>Answer</summary>

```bash
dig example.com
```

or:

```bash
nslookup example.com
```

</details>

---

## Scenario 6 — Find the route

You want to see the path toward a server.

**Question:** Which commands can help?

<details>
<summary>Answer</summary>

```bash
traceroute example.com
```

or:

```bash
tracepath example.com
```

</details>

---

## Scenario 7 — TCP or UDP?

A developer says the application needs reliable, ordered delivery.

**Question:** Which protocol is normally the better fit?

<details>
<summary>Answer</summary>

TCP.

</details>

---

## Scenario 8 — HTTP or HTTPS?

You are building a production web application.

**Question:** Which should normally be used for protecting traffic between the client and server?

<details>
<summary>Answer</summary>

HTTPS.

</details>

---

## Scenario 9 — DNS record

You want to find the IPv4 address associated with a hostname.

**Question:** Which DNS record are you looking for?

<details>
<summary>Answer</summary>

A record.

Command:

```bash
dig A example.com
```

</details>

---

## Scenario 10 — IPv6

You see:

```text
2001:db8::10
```

**Question:** Is this IPv4 or IPv6?

<details>
<summary>Answer</summary>

IPv6.

</details>

---

# 37. Quick Revision

## IP Addressing

```text
IP address = logical address of a network interface
```

## IPv4

```text
32-bit
Example: 192.168.1.10
```

## IPv6

```text
128-bit
Example: 2001:db8::1
```

## Private IP

```text
10.0.0.0/8
172.16.0.0/12
192.168.0.0/16
```

## CIDR

```text
192.168.1.10/24
```

`/24` means the first 24 bits represent the network prefix.

## Gateway

```text
The device used to reach other networks.
```

## DNS

```text
Domain name → IP address
```

## TCP

```text
Reliable + ordered + connection-oriented
```

## UDP

```text
Connectionless + low overhead
```

## HTTP

```text
Web application protocol
Default port: 80
```

## HTTPS

```text
HTTP over TLS
Default port: 443
```

## OSI

```text
7 Application
6 Presentation
5 Session
4 Transport
3 Network
2 Data Link
1 Physical
```

## Important Linux commands

```bash
ip addr
ip link
ip route
ip neigh
ping
ss
curl
wget
dig
nslookup
traceroute
tracepath
hostname
hostnamectl
nmcli
ethtool
```

---

# Final Mental Model

If you remember only one flow, remember this:

```text
                    USER
                      |
                      v
              Application
          (Browser / curl / API)
                      |
                      v
                   DNS
            Name → IP address
                      |
                      v
               IP Addressing
            Source → Destination
                      |
                      v
                  Routing
             Through gateway
                      |
                      v
             Transport Layer
              TCP or UDP
                      |
                      v
                  Port
             22 / 53 / 80 / 443
                      |
                      v
              Network Interface
                 MAC / Link
                      |
                      v
             Physical Network
```

And when troubleshooting:

```text
Interface
   ↓
IP address
   ↓
Route
   ↓
Gateway
   ↓
Destination
   ↓
DNS
   ↓
Port
   ↓
Service
   ↓
Application
```

This order prevents you from jumping directly into application debugging when the actual problem is something simple like a missing IP address, route, DNS record, or listening port.

---

# Beginner Study Order

Do not try to memorize everything at once.

Study in this order:

### Day 1
- What is a network?
- IP addresses
- IPv4
- Private vs public IP
- Subnet mask
- CIDR
- Gateway

### Day 2
- MAC addresses
- ARP
- IPv6
- DNS
- DNS resolution
- DNS records
- DHCP

### Day 3
- Ports
- Common ports
- TCP
- UDP
- TCP vs UDP

### Day 4
- HTTP
- HTTPS
- HTTP vs HTTPS
- OSI model
- TCP/IP model

### Day 5
Practice Linux commands:

```bash
ip addr
ip route
ip neigh
ping
ss
curl
dig
nslookup
traceroute
tracepath
hostname
nmcli
```

### Day 6+
Practice troubleshooting scenarios.

For every problem, ask:

```text
1. Does my interface work?
2. Do I have an IP?
3. Do I have a route?
4. Can I reach the gateway?
5. Can I reach the destination?
6. Does DNS resolve?
7. Is the required port open/listening?
8. Is the service running?
9. Is a firewall blocking it?
10. What do the application logs say?
```

That troubleshooting mindset is more important than memorizing individual commands.
