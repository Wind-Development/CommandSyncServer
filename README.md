# Command Sync Server

This plugin allows the Velocity proxy to run commands based on backend input.

## Purpose

The purpose of this is to allow easy access to the Velocity proxy instance's console. This is useful for many things including anticheat punishments, message broadcasting, and more!

## IMPORTANT!

It is HIGHLY recommended to setup a firewall for your sync server port (default 1500). This is much more secure than using the password authentication system.

## Setup

- Install this plugin on your Velocity instance.
- Install [Command Sync Client](https://github.com/Wind-Development/CommandSyncClient) on your backend server(s).
- Configure the Command Sync Client backend server name
- Setup is now complete! 

## Usage

- Running "/sync ban windcolor" on the backend server will ban the player windcolor (with a proper punishments plugin).
- This works with all sorts of commands and supports unlimited command lengths.

