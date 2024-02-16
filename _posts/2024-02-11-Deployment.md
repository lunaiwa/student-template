{
 "cells": [
  {
   "cell_type": "markdown",
   "metadata": {
    "dotnet_interactive": {
     "language": "csharp"
    },
    "polyglot_notebook": {
     "kernelName": "csharp"
    }
   },
   "source": [
    "---\n",
    "title: How to Deploy on AWS\n",
    "description: Lesson on AWS Deployment by RIFT. Made by Rachit\n",
    "toc: true\n",
    "layout: post\n",
    "---"
   ]
  },
  {
   "cell_type": "markdown",
   "metadata": {},
   "source": [
    "# Deployment\n",
    "\n",
    "**NOTE: Look at this [Cockpit blog](https://rift24.github.io/RIFT-Frontend/2024/01/30/Cockpit-Usage.html) for another way to access AWS Instances without being blocked by the school network.**\n",
    "\n",
    "**NOTE: If you are in CSP, ignore all maven commands (./mvnw), this is for spring boot. Your dockerfile already has everything needed to clean and update a flask server, ignore the maven commands**\n",
    "\n",
    "Ever wonder why you cannot use your really cool dynamic API page to transfer and store data?\n",
    "\n",
    "It obviously works on localhost, but you cannot access it from anywhere else. The address is also in some *stupid* (not really) form. Let's change that!"
   ]
  },
  {
   "cell_type": "markdown",
   "metadata": {},
   "source": [
    "## Accessing AWS Instances\n",
    "\n",
    "\n",
    "We deploy our instances through Amazon Elastic Compute Cloud (EC2), a service which allows us to have scalable web servers in the cloud. EC2 is better than a dedicated server or servers in a couple different ways, which are...\n",
    "- Deployment time reduced to mere minutes\n",
    "- Pay for what you use\n",
    "\n",
    "To access AWS EC2 Instances, you have to log into Amazon AWS with the credentials Mr. Mortensen provided you at the start of the year (if you don't have these, what are you doing?), which were your Github username and password. \n",
    "\n",
    "To depoly, you first sign in through this page: [AWS Console](https://aws.amazon.com/console/)\n",
    "\n",
    "![Deployment_AWS_Page_1](https://rackets-assets.vercel.app/assets/deployment_lesson/Deployment_AWS_Page_1.png)\n",
    "\n",
    "You will then be redirected to a page like this\n",
    "![Deployment_AWS_Page_2](https://rackets-assets.vercel.app/assets/deployment_lesson/Deployment_AWS_Page_2.png)\n",
    "\n",
    "Our AWS organization is called NighthawkCodingSociety, so in the Account ID tab of the sign in page, you will put \"nighthawkcodingsociety\". Then, you put in your Github username/password into their respective fields and log in.\n",
    "\n",
    "You will then be greeted by a screen similar to this\n",
    "![Deployment_AWS_Page_3](https://rackets-assets.vercel.app/assets/deployment_lesson/Deployment_AWS_Page_3.png)\n",
    "*I have a couple extra customizations on AWS since I have used it quite a bit before. I have starred EC2 and Route53 (coming later) so it appears in my taskbar, and since I use it a bit, I have a recently visited tab*\n",
    "\n",
    "Make sure you change your reigon from any default to US East (Ohio). This is where we will be running our servers.\n",
    "![Deployment_AWS_Page_4](https://rackets-assets.vercel.app/assets/deployment_lesson/Deployment_AWS_Page_4.png)\n",
    "\n",
    "To access the EC2 servers, go to the top search bar and type in \"EC2\" (you should star this so it appears in your favorites bar).Click on the starred one in the image below.\n",
    "![Deployment_AWS_Page_5](https://rackets-assets.vercel.app/assets/deployment_lesson/Deployment_AWS_Page_5.png)\n",
    "\n",
    "You will be greeted with a nice information page. Either on the sidebar or on the resources tab, click the Instances button. You will then be brought to a page similar to this.\n",
    "![Deployment_AWS_Page_6](https://rackets-assets.vercel.app/assets/deployment_lesson/Deployment_AWS_Page_6.png)\n",
    "![Deployment_AWS_Page_6.5](https://rackets-assets.vercel.app/assets/deployment_lesson/Deployment_AWS_Page_6-5.png)\n",
    "\n",
    "From here, we will be digressing shortly on how to start an AWS instance (in theory). We will all be sharing one deployed AWS Instance to reduce costs and system stress."
   ]
  },
  {
   "cell_type": "markdown",
   "metadata": {},
   "source": [
    "## Launching an Instance (Done for you)\n",
    "\n",
    "We go ahead and click the yellow/orange \"Launch Instance\" button on the top right to launch a new instance. There are many instances we can choose from, and each has their own benefits and drawbacks. We will be going with Ubuntu because it is the most applicable for us.\n",
    "[Different Linux Distributions](https://distrowatch.com/dwres.php?resource=family-tree)\n",
    "\n",
    "AWS Deployment Page:\n",
    "\n",
    "![Deployment_AWS_Page_7](https://rackets-assets.vercel.app/assets/deployment_lesson/Deployment_AWS_Page_7.png)\n",
    "\n",
    "\n",
    "\n",
    "\n",
    "Configurations:\n",
    "- Name (do not conflict names please)\n",
    "- Application and OS Machine Image set to Ubuntu 22.04 64 bit x86\n",
    "- Instance Type set to Default (t2.micro)\n",
    "- Key Pair (ssh), you do not need these (unless you want to connect over ssh), so set to \"Proceed without a key pair\"\n",
    "- Allow SSH (you do not want to change it later), HTTPS, and HTTP. It will ask you to make or use a new ruleset. You may use ours (it does what was just stated) called 'launch-wizard-145'.\n",
    "- Leave Configure Storage default\n",
    "- Launch one instance!\n",
    "\n",
    "We can then head back to the EC2 Homepage to Connect to the Instance!"
   ]
  },
  {
   "cell_type": "markdown",
   "metadata": {},
   "source": [
    "## Connect to an Instance\n",
    "\n",
    "We literally click the instance and press the connect button. Select EC2 Instance Connect (should be selected by default) and then press the connect button.\n",
    "![Deployment_AWS_Page_8](https://rackets-assets.vercel.app/assets/deployment_lesson/Deployment_AWS_Page_8.png)\n",
    "\n",
    "You will then be brought to the command prompt!\n",
    "![Deployment_AWS_Page_9](https://rackets-assets.vercel.app/assets/deployment_lesson/Deployment_AWS_Page_9.png)"
   ]
  },
  {
   "cell_type": "markdown",
   "metadata": {},
   "source": [
    "## Configuring an Instance (Done for you)\n",
    "\n",
    "To configure an instance, we have to do a couple of things. We have to...\n",
    "\n",
    "1. Install nginx (APT) as a reverse proxy for hosting multiple webpages\n",
    "2. Install certbot (SNAP) for eventually giving our sites SSL certificates\n",
    "3. Install docker (APT) - Run our website\n",
    "4. Install git (APT) - Get our website\n",
    "5. Configure a directory for sites (Organization)\n",
    "6. Configure nginx/everything to allow nginx\n",
    "\n",
    "To do these, we have documented our commands in a script and will be going over them in a demo."
   ]
  },
  {
   "cell_type": "code",
   "execution_count": null,
   "metadata": {},
   "outputs": [],
   "source": [
    "%%bash\n",
    "\n",
    "cd /\n",
    "\n",
    "sudo -s # Log into superuser\n",
    "apt-get update # Update APT repository list (knows what packages are on the web)\n",
    "# APT can be reconfigured through commands and sources.list but don't mess with it unless you know what you are doing, or you may break it\n",
    "\n",
    "apt-get install docker docker-compose nginx git -y # install docker-compose for docker, the nginx server that our api's will run on, and git \n",
    "# Technically git is already installed but for the purpose of the script we check\n",
    "snap install certbot --classic # SNAP adds the 'classic' flag to warn you about risks. You place the flag to accept them.\n",
    "# Cerbot uses SNAP because it is depricated on APT \n",
    "\n",
    "# Now we can organize our server with a designated deployments folder\n",
    "mkdir deployments\n",
    "\n",
    "# Now we configure the firewall\n",
    "ufw allow openssh # Allow SSH Access\n",
    "ufw allow 'Nginx Full' # Allow full Nginx access\n",
    "ufw delete allow 'Nginx HTTP' # This does not exist but we check to make sure there are no conflicting rules\n",
    "ufw enable # Turn on the firewall (off by default)\n",
    "\n",
    "# To test if the server is working\n",
    "curl localhost"
   ]
  },
  {
   "cell_type": "markdown",
   "metadata": {},
   "source": [
    "Curling our current localhost should return an html page that looks like this:\n",
    "```html\n",
    "<html>\n",
    "<head>\n",
    "<title>Welcome to nginx!</title>\n",
    "<style>\n",
    "    body {\n",
    "        width: 35em;\n",
    "        margin: 0 auto;\n",
    "        font-family: Tahoma, Verdana, Arial, sans-serif;\n",
    "    }\n",
    "</style>\n",
    "</head>\n",
    "<body>\n",
    "<h1>Welcome to nginx!</h1>\n",
    "<p>If you see this page, the nginx web server is successfully installed and\n",
    "working. Further configuration is required.</p>\n",
    "\n",
    "<p>For online documentation and support please refer to\n",
    "<a href=\"http://nginx.org/\">nginx.org</a>.<br/>\n",
    "Commercial support is available at\n",
    "<a href=\"http://nginx.com/\">nginx.com</a>.</p>\n",
    "\n",
    "<p><em>Thank you for using nginx.</em></p>\n",
    "</body>\n",
    "</html>\n",
    "```\n",
    "However, we will see it in html since HTML is processed into a webpage by the browser, while we are accessing it from a serverisde command line interface (CLI). We can now proceed onto the next step, which is deploying a physical github spring-boot server onto the backend. \n",
    "\n",
    "This overall step will be done for you since we are not going to be starting 20 different instances. We have created a new CSA instance for you to put your code on."
   ]
  },
  {
   "cell_type": "markdown",
   "metadata": {},
   "source": [
    "## Adding Websites, Part 1: Onto EC2\n",
    "\n",
    "This process should be somewhat familiar to y'all. Here is our procedure:\n",
    "1. Clone backend onto server\n",
    "2. Start up the backend with docker-compose so it runs on the server\n",
    "3. The docker-compose will open a port which we can use nginx to forward to\n",
    "4. Test the port to make sure our backend works\n",
    "5. Link the backend to nginx so it forwards our port to our (eventual) domain name\n",
    "\n",
    "So lets do it!\n",
    "\n",
    "Before we run our script, we run ```docker ps``` to see which ports are open for use, and we use ports not shown as ports that we can put our websites on (except other system and program reserved ports). "
   ]
  },
  {
   "cell_type": "code",
   "execution_count": null,
   "metadata": {},
   "outputs": [],
   "source": [
    "%%bash\n",
    "\n",
    "# Get backend in right spot\n",
    "cd /deployments \n",
    "git clone {mybackend.git}\n",
    "cd {mybackend}\n",
    "\n",
    "# Build and test backend\n",
    "docker-compose up -d --build\n",
    "curl localhost:{configured_port} # PORTS MUST NOT OVERLAP\n",
    "\n",
    "\n",
    "# IF you want to code a script, this is an example checkpoint\n",
    "# Checkpoint -- does the backend work?\n",
    "# put exact all caps YES to proceed or else the script will exit (safety)\n",
    "echo -n \"CHECKPOINT. Does the curl command return the right page? YES/NO: \" \n",
    "read checkpoint\n",
    "if [checkpoint == \"YES\"]; then\n",
    "    # In future scripts, we will not exit here, but assuming we have done R53 we will continue\n",
    "    echo \"CHECKPOINT PASSED. Proceed.\"\n",
    "    exit 0 # This will exit without displaying the output as an error\n",
    "else\n",
    "    echo \"CHECKPOINT FAILED. Must fix issues.\"\n",
    "    exit 1 # This will exit while displaying the output as an error\n",
    "fi"
   ]
  },
  {
   "cell_type": "markdown",
   "metadata": {},
   "source": [
    "## Adding Websites, Part 2: Route 53\n",
    "\n",
    "*You should get the IP address of your instance and save it somewhere before this. You will see why soon.*\n",
    "\n",
    "Navigate back to your AWS homepage, the one you just landed on when you logged in (click the AWS button in the top left corner). \n",
    "\n",
    "Now is the time for us to configure our domain names (or subdomains) so that we can actually see our site. Our reverse proxy needs the domain name to link our actual site to the name, and without that, our deployment will not work (there is a long story why, if you want to know please ask!).\n",
    "\n",
    "We use domain names to point text towards our IP's that are hosting our servers. The process is called DNS resolution.\n",
    "![AWS DNS](https://d1.awsstatic.com/Route53/how-route-53-routes-traffic.8d313c7da075c3c7303aaef32e89b5d0b7885e7c.png)\n",
    "*Taken from AWS*\n",
    "If you have any further questions about DNS, consult this link (and do some Googling y'all have it in you) before asking us (if a lot of people ask we will give an explanation of DNS in a blog): [AWS DNS Explanation](https://aws.amazon.com/route53/what-is-dns/).\n",
    "\n",
    "AWS Route 53 allows us to take one domain and split it into multiple domains while linking them to our EC2 instances by the IP Adresses and the nginx configuration of the instances. \n",
    "\n",
    "First, we go to the search bar and search \"Route 53\". We then click on it (I would also star it) so it takes us to the Route 53 Homepage.\n",
    "![Deployment_AWS_Page_10](https://rackets-assets.vercel.app/assets/deployment_lesson/Deployment_AWS_Page_10.png)\n",
    "*The one with the star*\n",
    "![Deployment_AWS_Page_11](https://rackets-assets.vercel.app/assets/deployment_lesson/Deployment_AWS_Page_11.png)\n",
    "\n",
    "Then, we click the page on the sidebar (or in the middle) called \"Hosted Zones\". This is where our domain configuration will be.\n",
    "![Deployment_AWS_Page_12](https://rackets-assets.vercel.app/assets/deployment_lesson/Deployment_AWS_Page_12.png)\n",
    "\n",
    "Our projects are student projects, so they will be subdividing the (division) domain stu.nighthawkcodingsociety.com (helps with organization with only one domain). Click on the blue link on AWS to take you to the next page.\n",
    "\n",
    "This next page is where we create our subdomain with a record. Add a subdomain title (you should save this somewhere too), and it must be something different than all the other subdomains shown on the records page. Then configure the record type as A, which routes traffic to IPv4 Addresses and (some) AWS Resources. In the value box, put in the public IPv4 address of your EC2 Instance (the one you have your website on). Everything else can remain default, and you can create your record.\n",
    "\n",
    "![Deployment_AWS_Page_13](https://rackets-assets.vercel.app/assets/deployment_lesson/Deployment_AWS_Page_13.png)\n",
    "![Deployment_AWS_Page_14](https://rackets-assets.vercel.app/assets/deployment_lesson/Deployment_AWS_Page_14.png)\n",
    "\n",
    "*Example ww3 site record*\n",
    "\n",
    "Route 53 is now configured. We can finally set up the middle man, or our reverse proxy server: nginx."
   ]
  },
  {
   "cell_type": "markdown",
   "metadata": {},
   "source": [
    "## Adding Websites, Part 3: nginx\n",
    "\n",
    "Navigate back to the EC2 Instance. We will now be configuring nginx for our site, which is the final part of the deployment stage. \n",
    "\n",
    "Step 1: Navigate to /etc/nginx/sites-availible\n",
    "\n",
    "```cd /etc/nginx/sites-available```\n",
    "\n",
    "Step 2: Make a unique project file\n",
    "\n",
    "```touch {someuniqueprojectname}```\n",
    "\n",
    "Step 3: Open the new file with nano\n",
    "\n",
    "```nano {someuniqueprojectname}```\n",
    "\n",
    "Step 4: Paste in the nginx configuration, modifying it for your site and what you have configured\n",
    "\n",
    "```\n",
    "server {\n",
    "   listen 80;\n",
    "    listen [::]:80;\n",
    "    server_name -----.stu.nighthawkcodingsociety.com ; # Change server name to the one on R53\n",
    "    # Configure CORS Headers\n",
    "    location / { \n",
    "        proxy_pass http://localhost:8---; # Change port to port on docker\n",
    "        # Simple requests\n",
    "        if ($request_method ~* \"(GET|POST|PUT|DELETE)\") { # Customize Request methods based on your needs\n",
    "                add_header \"Access-Control-Allow-Origin\"  *;\n",
    "        }\n",
    "        # Preflighted requests \n",
    "        if ($request_method = OPTIONS ) {\n",
    "                add_header \"Access-Control-Allow-Origin\"  *;\n",
    "                add_header \"Access-Control-Allow-Methods\" \"GET, POST, PUT, DELETE, OPTIONS, HEAD\"; # Make sure the request methods above match here\n",
    "                add_header \"Access-Control-Allow-Headers\" \"Authorization, Origin, X-Requested-With, Content-Type, Accept\";\n",
    "                return 200;\n",
    "        }\n",
    "    }\n",
    "}\n",
    "```\n",
    "\n",
    "**A quick explanation:** this nginx block is the configuration that links nginx to your docker process using a reverse proxy to forward the configured domain to the configured port. For CORS Headers, there are two modes: simple requests and preflighted requests. Simple requests are requests that have no implications and are not dangerous for the user or for the server, or simple data. Preflighted requests handle more complex and sensitive data, where the browser first sends a request through the OPTIONS method, and then the server responds to determine if the request is safe to proceed.\n",
    "\n",
    "\n",
    "There are two directives to this example: server_name and proxy_pass. \n",
    "\n",
    "server_name basically links the domain name to nginx for the server configuration. proxy_pass will then forward the requests made to the server with the \"tag\" for the domain name to the proxy_pass, which is the address for the localhost backend server corresponding to the domain name with server_name.\n",
    "\n",
    "Take it as nginx forwarding the domain to the port, acting as an intermediary, or the middle man.\n",
    "\n",
    "![Drawing](https://rackets-assets.vercel.app/assets/deployment_lesson/Drawing.png)\n",
    "\n",
    "Save changes by CTRL/CMD-X, y, ENTER\n",
    "\n",
    "Step 5: Activate configuration by linking the file to the active site directory. \n",
    "\n",
    "```cd /etc/nginx/sites-enabled```\n",
    "\n",
    "```ln -s /etc/nginx/sites-available/{someuniqueprojectname} /etc/nginx/sites-enabled/``` (as root)\n",
    "\n",
    "Step 6: Validation. nginx will check itself\n",
    "\n",
    "```nginx -t``` (as root)\n",
    "\n",
    "Step 7: Restart nginx for changes to take place\n",
    "\n",
    "```sudo systemctl restart nginx```\n",
    "\n",
    "```service nginx status``` (check status after)\n",
    "\n",
    "If all goes smoothley, you will now have an HTTP website deployed to AWS. Here is how we turn it into HTTPS (secure)."
   ]
  },
  {
   "cell_type": "markdown",
   "metadata": {},
   "source": [
    "## Certbot Configuration\n",
    "\n",
    "Certbot configuration is important because it gives us an SSL certificate, which allows our browser to trust our site. It keeps user data secure, verifies ownership of the website, prevents attackers from creating a fake version of the site, and conveys trust to users.\n",
    "\n",
    "![SSL Explanation](https://www.hostinger.com/tutorials/wp-content/uploads/sites/2/2022/01/how-SSL-certificates-work-dark.webp)\n",
    "*borrowed from hostinger*\n",
    "\n",
    "Guys, you literally have to run this command:\n",
    "\n",
    "```sudo certbot --nginx```\n",
    "\n",
    "and follow what it says. If it succeeds, GREAT. If not, NOT GREAT. Do what it says."
   ]
  },
  {
   "cell_type": "markdown",
   "metadata": {},
   "source": [
    "## Deployment Updates\n",
    "\n",
    "On your localhost, you should test your deployment by running:\n",
    "1. ```docker-compose down``` - Take down docker server so nothing breaks and it can recompile\n",
    "2. ```docker-compose up -d --build``` - Rebuild Docker\n",
    "\n",
    "Optional step between steps 1 & 2: ```./mvnw clean``` - Cleans maven (only for CSA)\n",
    "\n",
    "On EC2, update your deployment with the following commands:\n",
    "1. ```docker-compose down``` - Take down docker server so nothing breaks and it can recompile\n",
    "2. ```git pull``` - Fetch Latest Data\n",
    "3. ```./mvnw clean``` - Clean Maven (optional but recommended, only for CSA)\n",
    "4. ```docker-compose up -d --build``` - Rebuild docker\n",
    "5. IF no changes are shown, run ```service nginx restart```\n"
   ]
  },
  {
   "cell_type": "markdown",
   "metadata": {},
   "source": [
    "## Troubleshooting\n",
    "- ```curl localhost:{some port}``` (is this my page?)\n",
    "- ```docker-compose ps``` (is docker good?)\n",
    "- ```docker ps``` (is everything okay?)"
   ]
  },
  {
   "cell_type": "markdown",
   "metadata": {},
   "source": [
    "## Suggestions and Closure\n",
    "\n",
    "As you may have noticed, many of these things can be automated. Our team has automated many of these processes, and we encourage you to do the same. We will be posting our scripts through our blogs after a two days or so for you to learn how to deploy, and we will let you know when we do so. We encourage you to make your own to speed up the deployment process.\n",
    "\n",
    "We have created a new CSA server for everyone to deploy on because we do not want 60 instances. The framework and guidelines for using the instance are below, as well as on a seperate blog. \n",
    "\n",
    "If you have any questions, please message us on slack, ask chatgpt, or google the question. We will be blogging and maintaing the CSA server, just please don't destroy it. \n",
    "\n",
    "PS. We understand we can automate deployment updates through cron jobs and stuff. Shoot us a message if you have cool ideas or would like to help us impliment these."
   ]
  },
  {
   "cell_type": "markdown",
   "metadata": {},
   "source": [
    "## Framework of the CSA Server\n",
    "\n",
    "- Put all your deployed websites into ```/deployments``` (you will see ww3 there)\n",
    "- Don't mess with the ```/riftdev``` directory\n",
    "- Don't do something you will regret\n",
    "- Don't mess with other people's stuff\n",
    "- Don't crash the server and forget to tell us (PLEASE TELL US)"
   ]
  }
 ],
 "metadata": {
  "kernelspec": {
   "display_name": "Python 3",
   "language": "python",
   "name": "python3"
  },
  "language_info": {
   "codemirror_mode": {
    "name": "ipython",
    "version": 3
   },
   "file_extension": ".py",
   "mimetype": "text/x-python",
   "name": "python",
   "nbconvert_exporter": "python",
   "pygments_lexer": "ipython3",
   "version": "3.10.4"
  },
  "polyglot_notebook": {
   "kernelInfo": {
    "defaultKernelName": "csharp",
    "items": [
     {
      "aliases": [],
      "name": "csharp"
     }
    ]
   }
  }
 },
 "nbformat": 4,
 "nbformat_minor": 2
}