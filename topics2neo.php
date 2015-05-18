<?php
ini_set('memory_limit', -1);

include 'vendor/autoload.php';

function safePrint($data)
{
    foreach ($data as &$v) {
        if ($v instanceof stdClass) {
            $v = 'stdClass';
        }
    }

    print_r($data);
}

class Topics2Neo {

    /**
     * @var Everyman\Neo4j\Client
     */
    protected $database;


    public function __construct()
    {
        $client = new Everyman\Neo4j\Client('localhost', 7474);
        $client->getTransport()
            ->setAuth('neo4j', 'root');
        $this->database = $client;

        $this->importJson();
    }

    public function importJson()
    {
        $data = json_decode(file_get_contents('topictree.json'));
        $this->importNode($data);
    }

    public function importNode(stdClass $node, \Everyman\Neo4j\Node $parent = null)
    {
        $dbNode = $this->database->makeNode($nodeData = [
            'kind' => $node->kind,
            'name' => $node->title,
        ]);
        $dbNode->save();

        echo 'Importing node ' . $node->title . "\r\n";

        if (null !== $parent) {
            $this->database->makeRelationship()
                ->setStartNode($dbNode)
                ->setEndNode($parent)
                ->setType('CHILD_OF')
                ->save();
            echo 'New relationship ' . $dbNode->name . ' and ' . $parent->name . "\r\n";
        }


        if (isset($node->children) && is_array($node->children)) {
            foreach ($node->children as $child) {
                $this->importNode($child, $dbNode);
            }
        }
    }
}

new Topics2Neo();

