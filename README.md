## Blog site using GitHub Pages and Jekyll
> This site is intended for Students.   This is to record plans, complete hacks, and do work for your learnings.
- This can be customized to support computer science as you work through pathway (JavaScript, Python/Flask, Java/Spring)
- All tangible artifact work is in a _posts or in a _notebooks.  
- Front matter (aka meta data) in ipynb and md files is used to organize information according to week and column in running web site.

## GitHub Pages
All `GitHub Pages` websites are managed on GitHub infrastructure. GitHub uses `Jekyll` to tranform your content into static websites and blogs. Each time we change files in GitHub it initiates a GitHub Action that rebuilds and publishes the site with Jekyll.  
- GitHub Pages is powered by: [Jekyll](https://jekyllrb.com/).
- Publised teacher website: [nighthawkcoders.github.io/teacher](https://nighthawkcoders.github.io/teacher/)

## Preparing a Preview Site 
In all development, it is recommended to test your code before deployment.  The GitHub Pages development process is optimized by testing your development on your local machine, prior to files on GitHub

Development Cycle. For GitHub pages, the tooling described below will create a development cycle  `make-code-save-preview`.  In the development cycle, it is a requirement to preview work locally, prior to doing a VSCode `commit` to git.

Deployment Cycle.  In the deplopyment cycle, `sync-github-action-review`, it is a requirement to complete the development cycle prior to doing a VSCode `sync`.  The sync triggers github repository update.  The action starts the jekyll build to publish the website.  Any step can have errors and will require you to do a review.

### WSL and/or Ubuntu installation requirements
- The result of these step is Ubuntu tools to run preview server.  These procedures were created using [jekyllrb.com](https://jekyllrb.com/docs/installation/ubuntu/)
- Run scripts in scripts directory of teacher repo: setup_ubuntu.sh and activate.sh.  Or, follow commands below.
```bash
## WSL/Ubuntu commands
# sudo apt install, installs packages for Ubuntu
echo "=== Ugrade Packages ==="
sudo apt update
sudo apt upgrade -y
#
echo "=== Install Ruby ==="
sudo apt install -y ruby-full build-essential zlib1g-dev
# 
echo "=== Install Python ==="
sudo apt-get install -y python3 python3-pip python-is-python3
#    
echo "=== Install Jupyter Notebook ==="
sudo apt-get install -y jupyter-notebook

# bash commands, install user requirements.
echo "=== GitHub pages build tools  ==="
export GEM_HOME="$HOME/gems"
export PATH="$HOME/gems/bin:$PATH"
echo '# Install Ruby Gems to ~/gems' >> ~/.bashrc
echo 'export GEM_HOME="$HOME/gems"' >> ~/.bashrc
echo 'export PATH="$HOME/gems/bin:$PATH"' >> ~/.bashrc
echo "=== Gem install starting, thinking... ==="
gem install jekyll bundler
head -30 ./teacher/scripts/activate.sh
echo "=== !!!Start a new Terminal!!! ==="
```

### MacOs installation requirements 
- Ihe result of these step are MacOS tools to run preview server.  These procedures were created using [jekyllrb.com](https://jekyllrb.com/docs/installation/macos/). Run scripts in scripts directory of teacher repo: setup_macos.sh and activate_macos.sh.  Or, follow commands below.
```bash
# MacOS commands
# brew install, installs packages for MacOS
echo "=== Ugrade Packages ==="
brew update
brew upgrade
#
echo "=== Install Ruby ==="
brew install chruby ruby-install xz
ruby-install ruby 3.1.3
#
echo "=== Install Python ==="
brew install python
#    
echo "=== Install Jupyter Notebook ==="
brew install jupyter

# bash commands, install user requirements.
export GEM_HOME="$HOME/gems"
export PATH="$HOME/gems/bin:$PATH"
echo '# Install Ruby Gems to ~/gems' >> ~/.zshrc
echo 'export GEM_HOME="$HOME/gems"' >> ~/.zshrc
echo 'export PATH="$HOME/gems/bin:$PATH"' >> ~/.zshrc
echo "=== Gem install starting, thinking... ==="
gem install jekyll bundler
head -30 ./teacher/scripts/activate.sh
echo "=== !!!Start a new Terminal!!! ==="
```

### Preview
- The result of these step is server running on: http://0.0.0.0:4100/teacher/.  Regeneration messages will run in terminal on any save.  Press the Enter or Return key in the terminal at any time to enter commands.

- Complete installation
```bash
bundle install
```
- Run Server.  This requires running terminal commands `make`, `make stop`, `make clean`, or `make convert` to manage the running server.  Logging of details will appear in terminal.   A `Makefile` has been created in project to support commands and start processes.

    - Start preview server in terminal
    ```bash
    cd ~/vscode/teacher  # my project location, adapt as necessary
    make
    ```

    - Terminal output of shows server address. Cmd or Ctl click http location to open preview server in browser. Example Server address message... 
    ```
    Server address: http://0.0.0.0:4100/teacher/
    ```

    - Save on ipynb or md activiates "regeneration". Refresh browser to see updates. Example terminal message...
    ```
    Regenerating: 1 file(s) changed at 2023-07-31 06:54:32
        _notebooks/2024-01-04-cockpit-setup.ipynb
    ```

    - Terminal message are generated from background processes.  Click return or enter to obtain prompt and use terminal as needed for other tasks.  Alway return to root of project `cd ~/vscode/teacher` for all "make" actions. 
        

    - Stop preview server, but leave constructed files in project for your review.
    ```bash
    make stop
    ```

    - Stop server and "clean" constructed files, best choice when renaming files to eliminate potential duplicates in constructed files.
    ```bash
    make clean
    ```

    - Test notebook conversions, best choice to see if IPYNB conversion is acting up.
    ```bash
    make convert
    ```
