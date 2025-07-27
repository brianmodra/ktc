<?php

namespace Config;

use CodeIgniter\Config\Routing as BaseRouting;

/**
 * Routing Configuration
 */
class Routing extends BaseRouting
{
    /**
     * An array of directories in which to search for controllers.
     * These must be relative to APPPATH.
     */
    public array $controllers = ['Controllers'];

    /**
     * The default directory in which to search for controllers.
     * This must be relative to the Controllers directory.
     */
    public string $defaultController = 'Home';

    /**
     * The default method to use when no method is specified.
     */
    public string $defaultMethod = 'index';

    /**
     * Whether to translate URI dashes to underscores.
     */
    public bool $translateURIDashes = false;

    /**
     * The route filter to use if no route is found.
     */
    public ?string $override404 = null;

    /**
     * Whether to auto-route to controllers.
     */
    public bool $autoRoute = true;

    /**
     * Whether to use improved auto-routing.
     */
    public bool $autoRouteImproved = false;

    /**
     * The default namespace for controllers.
     */
    public string $defaultNamespace = 'App\Controllers';

    /**
     * Whether to force secure connections.
     */
    public bool $forceGlobalSecureRequests = false;

    /**
     * Default route priority.
     */
    public bool $prioritize = false;
} 